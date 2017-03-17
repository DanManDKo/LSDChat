package com.example.lsdchat.ui.main.createchat;


import android.content.Context;
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

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import de.hdodenhof.circleimageview.CircleImageView;


public class CreateChatRvAdapter extends RecyclerView.Adapter<CreateChatRvAdapter.ViewHolder> {

    private List<LoginUser> mLoginUserList;
    private CreateChatContract.Presenter mPresenter;
    private Map<String, String> mMapAvatar;
    private List<ContentModel> mContentModelList;
    private Set<Integer> idChecked;
    private Context mContext;

    public CreateChatRvAdapter(List<LoginUser> loginUserList, List<ContentModel> mContentModelList, CreateChatContract.Presenter presenter, Context context) {
        this.mLoginUserList = loginUserList;
        this.mPresenter = presenter;
        this.mContentModelList = mContentModelList;
        this.mContext = context;
        mMapAvatar = new HashMap<>();
        idChecked = new TreeSet<>();

        for (ContentModel user : mContentModelList) {
            mMapAvatar.put(user.getId(), user.getImagePath());
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recyler_new_chat, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LoginUser user = mLoginUserList.get(position);
        holder.mName.setText(user.getFullName());

        String path = mMapAvatar.get(String.valueOf(user.getId()));
        if (path != null) {
            holder.mImageView.setImageURI(Uri.fromFile(new File(path)));
        } else {
            holder.mImageView.setImageResource(R.drawable.userpic);
        }


        boolean isChecked = idChecked.contains(user.getId());
        holder.mCheckBox.setChecked(isChecked);
        if (isChecked) {
            holder.mName.setTextColor(mContext.getResources().getColor(R.color.colorNewChatTextCheck));
        } else {
            holder.mName.setTextColor(mContext.getResources().getColor(R.color.colorNewChatText));
        }


    }

    @Override
    public int getItemCount() {
        return mLoginUserList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView mImageView;
        TextView mName;
        CheckBox mCheckBox;


        ViewHolder(View itemView) {
            super(itemView);
            mImageView = (CircleImageView) itemView.findViewById(R.id.new_chat_member_image);
            mName = (TextView) itemView.findViewById(R.id.new_chat_member_name);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.new_chat_member_checkbox);


            mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    idChecked.add(mLoginUserList.get(getAdapterPosition()).getId());
                    mPresenter.checkBoxSetOnChecked(mLoginUserList.get(getAdapterPosition()).getId(), true);
                    mName.setTextColor(mContext.getResources().getColor(R.color.colorNewChatTextCheck));
                } else {
                    idChecked.remove(mLoginUserList.get(getAdapterPosition()).getId());
                    mPresenter.checkBoxSetOnChecked(mLoginUserList.get(getAdapterPosition()).getId(), false);
                    mName.setTextColor(mContext.getResources().getColor(R.color.colorNewChatText));

                }

            });
        }


    }
}
