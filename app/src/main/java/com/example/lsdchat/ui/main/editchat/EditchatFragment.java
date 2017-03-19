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
import android.widget.Toast;

import com.example.lsdchat.App;
import com.example.lsdchat.R;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.model.ContentModel;
import com.example.lsdchat.ui.PresenterLoader;
import com.example.lsdchat.ui.main.conversation.ConversationFragment;
import com.example.lsdchat.ui.main.fragment.BaseFragment;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class EditchatFragment extends BaseFragment implements EditchatContract.View {
    private static final String DIALOG_ID = "dialog_id";
    private static final int EDITCHAT_LOADER_ID = 101;
    private static final int REQUEST_IMAGE_CAMERA = 11;
    private static final int REQUEST_IMAGE_GALLERY = 22;

    private static final String TAG = "EDITCHAT_LOADER";

    private EditchatPresenter mPresenter;
    private EditchatAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private OnSaveButtonClicked mListener;

    private Toolbar mToolbar;
    private EditText mEditChatName;
    private SimpleDraweeView mDialogImage;
    private Button mSaveButton;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editchat, container, false);

        initView(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("fragment", "onActivityCreated");
        getLoaderManager().initLoader(EDITCHAT_LOADER_ID, null, new LoaderManager.LoaderCallbacks<EditchatPresenter>() {
            @Override
            public Loader<EditchatPresenter> onCreateLoader(int id, Bundle args) {
                Log.i(TAG, "onCreateLoader");
                return new PresenterLoader<EditchatPresenter>(getContext(), getFactory(), TAG);
            }

            @Override
            public void onLoadFinished(Loader<EditchatPresenter> loader, EditchatPresenter data) {
                Log.i(TAG, "onLoadFinished");
                EditchatFragment.this.mPresenter = data;

                onPresenterPrepared(mPresenter);
            }

            @Override
            public void onLoaderReset(Loader<EditchatPresenter> loader) {
                Log.i(TAG, "onLoaderReset");
                EditchatFragment.this.mPresenter = null;

                onPresenterDestroyed();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("fragment", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("fragment", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("fragment", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("fragment", "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("fragment", "onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("fragment", "onDetach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("fragment", "onDetach");
    }

    private void onPresenterPrepared(EditchatPresenter presenter) {
        int ownerID = App.getDataManager().getUser().getId();

        mAdapter = new EditchatAdapter(presenter, getContext(), ownerID);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        presenter.getAvatarsFromDatabase();
        presenter.loadDialogCredentials(getArguments().getString(DIALOG_ID));

        mDialogImage.setOnClickListener(v -> chooseDialogImage());
        mSaveButton.setOnClickListener(v ->
                mPresenter.updateDialogCredentials(mAdapter.getAddedOccupantsList(),
                        mAdapter.getDelatedOccupantsList(),
                        mEditChatName.getEditableText().toString()));
    }

    private void onPresenterDestroyed() {
        mListener = null;
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
        Toast.makeText(getContext(), "You can`t delete other users", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fillDialogAdapter(List<Integer> occupantIDs, List<LoginUser> appUsers, int type) {
        mAdapter.setOccupantsList(occupantIDs);
        mAdapter.setUsersList(appUsers);
        mAdapter.setDialogType(type);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void navigateToConversationFragment(String dialogID, String dialogName) {
        mListener.onConversationFragmentSelected(dialogID, dialogName);
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
        void onConversationFragmentSelected(String dialogID, String dialogName);
    }

}

