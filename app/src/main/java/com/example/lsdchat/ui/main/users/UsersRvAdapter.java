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
import com.example.lsdchat.model.UserAvatar;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UsersRvAdapter extends RecyclerView.Adapter<UsersRvAdapter.ViewHolder> {
    private List<LoginUser> data;
    private List<UserAvatar> userAvatars;
    private UsersContract.Presenter mPresenter;
    private Map<Integer, String> mapAvatar;

    public UsersRvAdapter(List<LoginUser> data, UsersContract.Presenter presenter, List<UserAvatar> userAvatars) {
        this.userAvatars = userAvatars;
        this.data = data;
        this.mPresenter = presenter;
        mapAvatar = new HashMap<>();

        for (UserAvatar user: userAvatars) {
            mapAvatar.put(user.getUserId(),user.getImagePath());
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_recycler_users, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        LoginUser userQuick = data.get(i);
        String path = mapAvatar.get(userQuick.getId());

        if (path != null) {
            viewHolder.mImageView.setImageURI(Uri.fromFile(new File(path)));
        }
        viewHolder.mName.setText(userQuick.getFullName());

        viewHolder.mRlUser.setOnClickListener(v -> mPresenter.setClickUser(userQuick));



    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView mImageView;
        TextView mName;
        RelativeLayout mRlUser;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (SimpleDraweeView) itemView.findViewById(R.id.users_image);
            mName = (TextView) itemView.findViewById(R.id.users_name);
            mRlUser = (RelativeLayout) itemView.findViewById(R.id.rl_user);

        }
    }
}
