package com.example.lsdchat.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.lsdchat.R;
import com.example.lsdchat.model.ContactsModel;
import com.example.lsdchat.manager.SharedPreferencesManager;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class CreateChatActivity extends AppCompatActivity implements CreateChatContract.View {

    private EditText mNameChat;
    private TextInputLayout mIlName;
    private RadioButton mRbPublic;
    private RadioButton mRbPrivate;
    private RecyclerView mRvSelectMembers;
    private Button mBtnCreate;
    private SimpleDraweeView mImageView;
    private CreateChatPresenter mCreateChatPresenter;

    private CreateChatRvAdapter mCreateChatRvAdapter;
    private List<ContactsModel> mContactsModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCreateChatPresenter = new CreateChatPresenter(this, new SharedPreferencesManager(this));

        mCreateChatPresenter.getContactsModel();
        setContentView(R.layout.fragment_new_chat);

        initView();
        initAdapter();


        mCreateChatPresenter.btnCreateClick(mBtnCreate, mNameChat);
        mCreateChatPresenter.btnImageClick(mImageView);
    }

    @Override
    public void initAdapter() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRvSelectMembers.setLayoutManager(mLayoutManager);
        mCreateChatRvAdapter = new CreateChatRvAdapter();
        mRvSelectMembers.setAdapter(mCreateChatRvAdapter);

    }

    @Override
    public void addModel(List<ContactsModel> list) {
        for (ContactsModel contactsModel: list){
            mCreateChatRvAdapter.add(contactsModel);
        }
        mCreateChatRvAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mNameChat = (EditText) findViewById(R.id.input_chat_name);
        mIlName = (TextInputLayout) findViewById(R.id.input_layout_chat_name);
        mRbPublic = (RadioButton) findViewById(R.id.radio_public);
        mRbPrivate = (RadioButton) findViewById(R.id.radio_private);
        mRvSelectMembers = (RecyclerView) findViewById(R.id.new_chat_members_container);
        mBtnCreate = (Button) findViewById(R.id.new_chat_button_create);
        mImageView = (SimpleDraweeView) findViewById(R.id.iv_user_reg);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCreateChatPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCreateChatPresenter.onDestroy();
    }

    @Override
    public void showDialogImageSourceChooser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
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
        return this;
    }
}
