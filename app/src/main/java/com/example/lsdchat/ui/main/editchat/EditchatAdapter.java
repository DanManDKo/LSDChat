package com.example.lsdchat.ui.main.editchat;


import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.lsdchat.R;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.model.ContentModel;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditchatAdapter extends RecyclerView.Adapter<EditchatAdapter.ViewHolder> {
    private static final int PUBLIC_GROUP_TYPE = 1;
    private static final int PRIVATE_GROUP_TYPE = 2;
    private static final int PRIVATE_TYPE = 3;
    private static final String EMPTY_STRING = "";

    private List<Integer> mOccupantsList;
    private List<LoginUser> mUsersList;
    private List<ContentModel> mContentModelList;
    private Map<String, String> mMapAvatar;

    private int mDialogType;
    private EditchatPresenter mPresenter;

    public EditchatAdapter(EditchatPresenter presenter, List<ContentModel> contentModelList) {
        mOccupantsList = new ArrayList<>();
        mUsersList = new ArrayList<>();
        mDialogType = PUBLIC_GROUP_TYPE;
        mPresenter = presenter;
        mContentModelList = contentModelList;

        mMapAvatar = new HashMap<>();

        for (ContentModel contentModel : mContentModelList) {
            mMapAvatar.put(contentModel.getId(), contentModel.getImagePath());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_recycler_editchat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LoginUser user = mUsersList.get(position);
        if (user != null) {
            holder.mUserName.setText(user.getFullName());

            String path = mMapAvatar.get(String.valueOf(user.getId()));
            if (path != null) {
                holder.mUserAvatar.setImageURI(Uri.fromFile(new File(path)));
            } else {
                holder.mUserAvatar.setImageResource(R.drawable.userpic);
            }

            switch (mDialogType) {
                case PUBLIC_GROUP_TYPE:
                    holder.mCheckBox.setVisibility(View.GONE);
                case PRIVATE_GROUP_TYPE:
                case PRIVATE_TYPE:
                    if (mOccupantsList.contains(user.getId())) holder.mCheckBox.setChecked(true);
                    holder.mCheckBox.setOnClickListener(view ->
                            mPresenter.setOnCheckedChangeListener((CheckBox)view, user.getId()));

            }
        }
    }

    @Override
    public int getItemCount() {
        return mUsersList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView mUserAvatar;
        private TextView mUserName;
        private CheckBox mCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);
            mUserAvatar = (CircleImageView) itemView.findViewById(R.id.item_editchat_usericon);
            mUserName = (TextView) itemView.findViewById(R.id.item_editchat_username);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.item_editchat_checkbox);
        }
    }

    public void setOccupantsList(List<Integer> occupantsList) {
        mOccupantsList.addAll(occupantsList);
    }

    public void setUsersList(List<LoginUser> usersList) {
        mUsersList.addAll(usersList);
    }

    public void setDialogType(int dialogType) {
        mDialogType = dialogType;
    }
}
