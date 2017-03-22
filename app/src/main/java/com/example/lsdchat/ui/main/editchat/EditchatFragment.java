package com.example.lsdchat.ui.main.editchat;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.lsdchat.App;
import com.example.lsdchat.R;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.model.ContentModel;
import com.example.lsdchat.ui.PresenterLoader;
import com.example.lsdchat.util.error.NetworkConnect;
import com.example.lsdchat.ui.main.fragment.BaseFragment;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
import java.util.Set;

import static android.app.Activity.RESULT_OK;

public class EditchatFragment extends BaseFragment implements EditchatContract.View {
    private static final String DIALOG_ID = "dialog_id";
    private static final int EDITCHAT_LOADER_ID = 101;
    private static final int REQUEST_IMAGE_CAMERA = 11;
    private static final int REQUEST_IMAGE_GALLERY = 22;

    private static final String TAG = "EDITCHAT_LOADER";

    private EditchatContract.Presenter mPresenter;
    private EditchatAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private OnSaveButtonClicked mListener;

    private Toolbar mToolbar;
    private EditText mEditChatName;
    private SimpleDraweeView mDialogImage;
    private Button mSaveButton;
    private RelativeLayout mRlUsers;
    private NetworkConnect networkConnect;
    private String dialogId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (EditchatFragment.OnSaveButtonClicked) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity should implement " + EditchatFragment.OnSaveButtonClicked.class.getSimpleName());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        networkConnect = ((NetworkConnect) getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editchat, container, false);
        dialogId = getArguments().getString(DIALOG_ID);
        initView(view);
        mPresenter = new EditchatPresenter(this,new EditchatModel(),App.getSharedPreferencesManager(getActivity()));
        onPresenterPrepared();
        return view;
    }

    @Override
    public boolean isNetworkConnect() {
        return networkConnect.isNetworkConnect();
    }


    private void onPresenterPrepared() {
//        int ownerID = App.getDataManager().getUser().getId();
        mPresenter.getAvatarsFromDatabase();
        mPresenter.loadDialogCredentials(getArguments().getString(DIALOG_ID));

//        initUserList();
        mAdapter = new EditchatAdapter(mPresenter, getContext());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);


        mDialogImage.setOnClickListener(v -> chooseDialogImage());
        mSaveButton.setOnClickListener(v ->
                mPresenter.updateDialogCredentials(mEditChatName.getEditableText().toString()));
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void initOccupantsIdList(Set<Integer> occupantsId) {
        mAdapter.setIdChecked(occupantsId);
        Log.e("UUU", String.valueOf(occupantsId.size()));
    }

    private void onPresenterDestroyed() {
        mListener = null;
        mPresenter.onDetach();
    }

    @Override
    public void showDialogError(Throwable throwable) {
        dialogError(throwable);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void fillDialogNameField(String name, Integer dialogCreaterID) {
        mEditChatName.setText(name);
        mAdapter.setDialogCreaterID(dialogCreaterID);
    }

    @Override
    public void fillAdapterContentModelsList(List<ContentModel> contentModels) {
        mAdapter.setContentModelList(contentModels);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showDialogAvatar(Uri path) {
        mDialogImage.setImageURI(path);
    }

    @Override
    public void showPermissionErrorMessage() {
        dialogError("You can`t delete other users");
//        Toast.makeText(getContext(), "You can`t delete other users", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fillDialogAdapter(List<Integer> occupantIDs, List<LoginUser> appUsers, int type) {
//        mAdapter.setOccupantsList(occupantIDs);
        mAdapter.setUsersList(appUsers);
        mAdapter.setDialogType(type);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void navigateToConversationFragment(String dialogID, String dialogName, int dialogType, int singleOccupant) {
        mListener.onConversationFragmentSelected(dialogID, dialogName, dialogType, singleOccupant);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_CAMERA:
                if (resultCode == RESULT_OK) {
                    mDialogImage.setImageURI(mPresenter.getDialogImageUri());
                    mPresenter.saveDialogImageUri(mPresenter.getDialogImageUri());
                }
                break;
            case REQUEST_IMAGE_GALLERY:
                if (resultCode == RESULT_OK) {
                    mDialogImage.setImageURI(data.getData());
                    mPresenter.saveDialogImageUri(data.getData());
                }
                break;
        }
    }

    private void chooseDialogImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.add_photo))
                .setPositiveButton(getString(R.string.photo_gallery), (dialogInterface, i) -> {
                    mPresenter.getPhotoFromGallery();
                })
                .setNegativeButton(getString(R.string.device_camera), (dialogInterface, i) -> {
                    mPresenter.getPhotoFromCamera();
                });
        builder.create().show();
    }

    private void initView(View view) {
        mEditChatName = (EditText) view.findViewById(R.id.editchat_et_chatname);
        mSaveButton = (Button) view.findViewById(R.id.editchat_button);
        mDialogImage = (SimpleDraweeView) view.findViewById(R.id.editchat_groupeimage);
        mToolbar = (Toolbar) view.findViewById(R.id.chats_toolbar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.editchat_recycler);
        mRlUsers = (RelativeLayout) view.findViewById(R.id.rl_user_edit_chat);


        initToolbar(mToolbar, getString(R.string.edit_chat_title));
    }

    private EditchatPresenterFactory getFactory() {
        EditchatContract.Model model = new EditchatModel();
        return new EditchatPresenterFactory(model, this, App.getSharedPreferencesManager(getContext()));
    }

    public static EditchatFragment newInstance(String dialogID) {
        EditchatFragment fragment = new EditchatFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DIALOG_ID, dialogID);
        fragment.setArguments(bundle);
        return fragment;
    }

    public interface OnSaveButtonClicked {
        void onConversationFragmentSelected(String dialogID, String dialogName, int dialogType, int singleOccupant);
    }


    @Override
    public void setRlUsersAccessibility(boolean enable) {
        if (enable) {
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
        }
    }

}

