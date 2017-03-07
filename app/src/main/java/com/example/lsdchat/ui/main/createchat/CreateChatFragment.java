package com.example.lsdchat.ui.main.createchat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.lsdchat.App;
import com.example.lsdchat.R;
import com.example.lsdchat.model.ContactsModel;
import com.example.lsdchat.ui.main.fragment.BaseFragment;
import com.example.lsdchat.util.ErrorsCode;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class CreateChatFragment extends BaseFragment implements CreateChatContract.View {

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
        View view = inflater.inflate(R.layout.fragment_new_chat,container,false);
        mCreateChatPresenter = new CreateChatPresenter(this, App.getSharedPreferencesManager(getActivity()));

        initView(view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRvSelectMembers.setLayoutManager(mLayoutManager);

        mCreateChatPresenter.getContactsModel();
        mCreateChatPresenter.setOnCheckedChangeListener(mRbPrivacy);


        mCreateChatPresenter.btnCreateClick(mBtnCreate, mNameChat);
        mCreateChatPresenter.btnImageClick(mImageView);

        return view;
    }

    @Override
    public void initAdapter(List<ContactsModel> list) {

        mCreateChatRvAdapter = new CreateChatRvAdapter(list,mCreateChatPresenter);
        mRvSelectMembers.setAdapter(mCreateChatRvAdapter);
        mCreateChatRvAdapter.notifyDataSetChanged();

    }


    @Override
    public void addModel(List<ContactsModel> list) {
        for (ContactsModel contactsModel : list) {
            mCreateChatRvAdapter.add(contactsModel);
        }
        mCreateChatRvAdapter.notifyDataSetChanged();
    }

    @Override
    public void setRecyclerEnableDisable(boolean enable) {
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

        initToolbar(mToolbar,"New Chat");
        mToolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
    }

    @Override
    public String getChatName() {
        return mNameChat.getText().toString();
    }

    @Override
    public boolean isRbPublic() {
        return mRbPublic.isChecked();
    }

    @Override
    public boolean isRbPrivate() {
        return mRbPrivate.isChecked();
    }

    @Override
    public void setNameError() {
        mIlName.setError(getString(R.string.chat_error_name));
    }

    @Override
    public void hideNameError() {
        mIlName.setError(null);
    }

    @Override
    public void getUserpicUri(Uri uri) {
        mImageView.setImageURI(uri);
    }

    @Override
    public void setEnableName(boolean enableName) {
        if (enableName) {
            mNameChat.setEnabled(true);
        } else {
            mNameChat.setEnabled(false);
        }
    }

    @Override
    public void setEnableImage(boolean enableImage) {
        if (enableImage) {
            mImageView.setEnabled(true);
        } else {
            mImageView.setEnabled(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCreateChatPresenter.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        mCreateChatPresenter.onDestroy();
    }

    @Override
    public void showDialogImageSourceChooser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.add_photo))
                .setPositiveButton(getString(R.string.photo_gallery), (dialogInterface, i) -> {
                    mCreateChatPresenter.getPhotoFromGallery();
                })
                .setNegativeButton(getString(R.string.device_camera), (dialogInterface, i) -> {
                    mCreateChatPresenter.getPhotoFromCamera();
                });
        builder.create().show();
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void showErrorDialog(String message) {
        ErrorsCode.showErrorDialog(getActivity(), message);
    }
}
