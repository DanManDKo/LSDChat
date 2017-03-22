package com.example.lsdchat.ui.main.chats.dialogs;


import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lsdchat.R;
import com.example.lsdchat.model.ContentModel;
import com.example.lsdchat.model.RealmDialogModel;
import com.example.lsdchat.util.DateUtils;
import com.example.lsdchat.util.UsersUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DialogsAdapter extends RecyclerView.Adapter<DialogsAdapter.ViewHolder> {

    private List<RealmDialogModel> mData;
    private DialogsContract.Presenter mPresenter;

    private List<ContentModel> mContentModelList;
    private Map<String, String> mMapAvatar;

    public DialogsAdapter(DialogsContract.Presenter mPresenter) {
        mData = new ArrayList<>();
        this.mPresenter = mPresenter;
        mContentModelList = new ArrayList<>();

        mMapAvatar = new HashMap<>();


    }

    public void setContentModelList(List<ContentModel> contentModelList) {
        mContentModelList.addAll(contentModelList);

        for (ContentModel user : mContentModelList) {
            mMapAvatar.put(user.getId(), user.getImagePath());
        }

        notifyDataSetChanged();
    }

    public void addData(List<RealmDialogModel> loginUserList) {
        mData.addAll(loginUserList);
        notifyDataSetChanged();
    }

    public void deleteItemData(RealmDialogModel dialogModel) {
        if (mData.contains(dialogModel)) {
            mData.remove(dialogModel);
        }
        notifyDataSetChanged();
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_dialog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RealmDialogModel realmDialogModel = mData.get(position);
        if (realmDialogModel != null) {
            holder.mChatName.setText(realmDialogModel.getName());
            holder.mChatDate.setText(DateUtils.millisecondsToDate(realmDialogModel.getLastMessageDateSent()));
            if (realmDialogModel.getLastMessageUserId() != null) {
            String fullName = UsersUtil.getUserById(realmDialogModel.getLastMessageUserId()).getFullName();
            if (fullName!=null) {
                holder.mChatTitle.setText(fullName);
            }
            } else {
                holder.mChatTitle.setText("");
            }
            holder.mChatLastMessage.setText(realmDialogModel.getLastMessage());

            if (realmDialogModel.getUnreadMessagesCount() != null) {
                if (realmDialogModel.getUnreadMessagesCount() != 0) {
                    holder.mNewMessageCounter.setVisibility(View.VISIBLE);
                    holder.mChatName.setTextColor(Color.parseColor("#327780"));
                    holder.mChatDate.setTextColor(Color.BLACK);
                    holder.mNewMessageCounter.setText(String.valueOf(realmDialogModel.getUnreadMessagesCount()));
                } else {
                    holder.mChatName.setTextColor(Color.parseColor("#000000"));
                    holder.mNewMessageCounter.setText("");
                    holder.mNewMessageCounter.setVisibility(View.GONE);
                }

            }
            String path = mMapAvatar.get(realmDialogModel.getId());
            if (path != null) {
                holder.mDialogImage.setImageURI(Uri.fromFile(new File(path)));
            } else {
                holder.mDialogImage.setImageResource(R.drawable.userpic_group);
            }

            holder.mRl.setOnClickListener(v -> mPresenter.setClickRl(realmDialogModel));
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mNewMessageCounter;
        CircleImageView mDialogImage;
        TextView mChatName;
        TextView mChatDate;
        TextView mChatTitle;
        TextView mChatLastMessage;
        RelativeLayout mRl;

        public ViewHolder(View view) {
            super(view);
            mNewMessageCounter = (TextView) view.findViewById(R.id.recycler_chat_new_message_counter);
            mDialogImage = (CircleImageView) view.findViewById(R.id.recycler_chat_logo);
            mChatName = (TextView) view.findViewById(R.id.recycler_chat_name);
            mChatDate = (TextView) view.findViewById(R.id.recycler_chat_date);
            mChatTitle = (TextView) view.findViewById(R.id.recycler_chat_title);
            mChatLastMessage = (TextView) view.findViewById(R.id.recycler_chat_last_message);
            mRl = (RelativeLayout) view.findViewById(R.id.recycler_rl);
        }
    }
}
