package com.example.lsdchat.ui.main.createchat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.lsdchat.App;
import com.example.lsdchat.R;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.model.ContentModel;
import com.example.lsdchat.ui.main.fragment.BaseFragment;
import com.example.lsdchat.util.StorageHelper;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class CreateChatFragment extends BaseFragment implements CreateChatContract.View {

    private static final int REQUEST_IMAGE_CAMERA = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
    private static final String AVATAR_FILE_NAME = "_icon.jpg";
    private Uri mFullSizeAvatarUri = null;
    private File mUploadFile = null;

    private EditText mNameChat;
    private TextInputLayout mIlName;
    private RadioButton mRbPublic;
    private RadioButton mRbPrivate;
    private RecyclerView mRvSelectMembers;
    private Button mBtnCreate;
    private SimpleDraweeView mImageView;
    private CreateChatPresenter mCreateChatPresenter;
    private RadioGroup mRbPrivacy;
    private CreateChatRvAdapter mCreateChatRvAdapter;
    private LinearLayout mLlSelectMembers;
    private Toolbar mToolbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_chat, container, false);

        mCreateChatPresenter =
                new CreateChatPresenter(this,
                        new CreateChatModel(App.getSharedPreferencesManager(getActivity()),
                                App.getDataManager(),
                                App.getApiManager().getDialogService(),
                                App.getApiManager().getRegistrationService()));

        initView(view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRvSelectMembers.setLayoutManager(mLayoutManager);

        mCreateChatRvAdapter = new CreateChatRvAdapter(mCreateChatPresenter, getActivity());
        mRvSelectMembers.setAdapter(mCreateChatRvAdapter);


        mCreateChatPresenter.getUserListObservable();

        mCreateChatPresenter.getObservableUserAvatar();

        radioGroupIsChecked();


        mBtnCreate.setOnClickListener(v -> mCreateChatPresenter.setClickCreateNewDialog(mUploadFile));
        mImageView.setOnClickListener(v -> showDialogImageChoose());

        return view;
    }

    private void radioGroupIsChecked() {
        mRbPrivacy.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_public:
                    setUsersListAccessibility(false);
                    setNameAccessibility(true);
                    setImageAccessibility(true);

                    break;
                case R.id.radio_private:
                    setUsersListAccessibility(true);
                    break;
            }

        });
    }

    @Override
    public void setListUsers(List<LoginUser> list) {
        mCreateChatRvAdapter.setUserList(list);
    }

    @Override
    public void setContentModelList(List<ContentModel> contentModelList) {
        mCreateChatRvAdapter.setContentModelList(contentModelList);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setUsersListAccessibility(boolean enable) {
        if (!enable) {
            mLlSelectMembers.setVisibility(View.GONE);
        } else {
            mLlSelectMembers.setVisibility(View.VISIBLE);
        }
    }

    private void initView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.chats_toolbar);
        mNameChat = (EditText) view.findViewById(R.id.input_chat_name);
        mIlName = (TextInputLayout) view.findViewById(R.id.input_layout_chat_name);
        mRbPublic = (RadioButton) view.findViewById(R.id.radio_public);
        mRbPrivate = (RadioButton) view.findViewById(R.id.radio_private);
        mRbPrivacy = (RadioGroup) view.findViewById(R.id.new_chat_privacy_container);
        mRvSelectMembers = (RecyclerView) view.findViewById(R.id.new_chat_members_container);
        mBtnCreate = (Button) view.findViewById(R.id.new_chat_button_create);
        mImageView = (SimpleDraweeView) view.findViewById(R.id.iv_user_reg);
        mLlSelectMembers = (LinearLayout) view.findViewById(R.id.ll_select_members);

        initToolbar(mToolbar, "New Chat");
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }


    @Override
    public boolean isRbPublic() {
        return mRbPublic.isChecked();
    }

    @Override
    public boolean isRbPrivate() {
        return mRbPrivate.isChecked();
    }


    private void getUserpicUri(Uri uri) {
        mImageView.setImageURI(uri);
    }

    @Override
    public String getNameDialog() {
        return mNameChat.getText().toString();
    }

    @Override
    public void setNameAccessibility(boolean enable) {
        if (enable) {
            mNameChat.setEnabled(true);
        } else {
            mNameChat.setEnabled(false);
        }
    }

    @Override
    public void setImageAccessibility(boolean enable) {
        if (enable) {
            mImageView.setEnabled(true);
        } else {
            mImageView.setEnabled(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAMERA:
                if (resultCode == RESULT_OK) {
                    getUserpicUri(mFullSizeAvatarUri);
                    try {
                        mUploadFile = StorageHelper.decodeAndSaveUri(getActivity(), mFullSizeAvatarUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getActivity(), R.string.photo_added, Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_IMAGE_GALLERY:
                if (resultCode == RESULT_OK) {
                    mFullSizeAvatarUri = data.getData();
                    getUserpicUri(mFullSizeAvatarUri);
                    try {
                        mUploadFile = StorageHelper.decodeAndSaveUri(getActivity(), mFullSizeAvatarUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getActivity(), R.string.photo_added, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mCreateChatPresenter.onDestroy();
    }

    private void showDialogImageChoose() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.add_photo))
                .setPositiveButton(getString(R.string.photo_gallery), (dialogInterface, i) -> getPhotoFromGallery())
                .setNegativeButton(getString(R.string.device_camera), (dialogInterface, i) -> getPhotoFromCamera());
        builder.create().show();
    }

    private void getPhotoFromGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_IMAGE_GALLERY);
    }

    private void getPhotoFromCamera() {
        if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (pictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

                File storageDir = getActivity().getExternalFilesDir(String.valueOf(Environment.DIRECTORY_PICTURES));
                String timestamp = new SimpleDateFormat(DATE_FORMAT).format(new Date());
                File file = new File(storageDir + "/" + timestamp + AVATAR_FILE_NAME);
                mFullSizeAvatarUri = Uri.fromFile(file);

                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mFullSizeAvatarUri);
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAMERA);
            }
        } else {
            Toast.makeText(getActivity(), R.string.there_is_no_device_camera, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showErrorDialog(int messageId) {
        String errorMessage = getResources().getString(messageId);
        dialogError(errorMessage);
    }

    @Override
    public void showErrorDialog(Throwable throwable) {
        dialogError(throwable);
    }

    @Override
    public void navigateToChat(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
    }


}
