package com.example.lsdchat.ui.main.users;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lsdchat.R;
import com.example.lsdchat.api.login.model.LoginUser;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class UsersRvAdapter extends RecyclerView.Adapter<UsersRvAdapter.ViewHolder> {
    private List<LoginUser> data;
    private UsersContract.Presenter presenter;

    public UsersRvAdapter(List<LoginUser> data, UsersContract.Presenter presenter) {

        this.data = data;
        this.presenter = presenter;

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

        presenter.setImageView(viewHolder.mImageView,userQuick.getBlobId());

        viewHolder.mName.setText(userQuick.getFullName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setFilter(List<LoginUser> countryModels) {
        data = new ArrayList<>();
        data.addAll(countryModels);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView mImageView;
        TextView mName;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (CircleImageView) itemView.findViewById(R.id.users_image);
            mName = (TextView) itemView.findViewById(R.id.users_name);

        }
    }
}
