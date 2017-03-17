package com.example.lsdchat.ui.main.users;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lsdchat.R;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.model.ContentModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class UsersRvAdapter extends RecyclerView.Adapter<UsersRvAdapter.ViewHolder> {
    private List<LoginUser> mLoginUserList;
    private List<ContentModel> mContentModelList;
    private UsersContract.Presenter mPresenter;
    private Map<String, String> mMapAvatar;

    public UsersRvAdapter(UsersContract.Presenter presenter) {
        mContentModelList = new ArrayList<>();
        mLoginUserList = new ArrayList<>();
        this.mPresenter = presenter;
        mMapAvatar = new HashMap<>();



    }

    public void setContentModelList(List<ContentModel> contentModelList) {
        mContentModelList.addAll(contentModelList);

        for (ContentModel user: mContentModelList) {
            mMapAvatar.put(user.getId(),user.getImagePath());
        }

        notifyDataSetChanged();
    }

    public void addData(List<LoginUser> loginUserList) {
        mLoginUserList.addAll(loginUserList);
        notifyDataSetChanged();
    }

    public void clearData() {
        mLoginUserList.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_recycler_users, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        LoginUser userQuick = mLoginUserList.get(i);

        String path = mMapAvatar.get(String.valueOf(userQuick.getId()));
        if (path != null) {
            viewHolder.mImageView.setImageURI(Uri.fromFile(new File(path)));
        }
        else {
            viewHolder.mImageView.setImageResource(R.drawable.userpic);
        }
        viewHolder.mName.setText(userQuick.getFullName());

        viewHolder.mRlUser.setOnClickListener(v -> mPresenter.setClickUser(userQuick));



    }

    @Override
    public int getItemCount() {
        return mLoginUserList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView mImageView;
        TextView mName;
        RelativeLayout mRlUser;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (CircleImageView) itemView.findViewById(R.id.users_image);
            mName = (TextView) itemView.findViewById(R.id.users_name);
            mRlUser = (RelativeLayout) itemView.findViewById(R.id.rl_user);

        }
    }
}
