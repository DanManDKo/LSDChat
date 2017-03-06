package com.example.lsdchat.ui.main.chats.dialogs;


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lsdchat.R;
import com.example.lsdchat.model.DialogModel;
import com.example.lsdchat.util.DateUtils;
import com.example.lsdchat.util.UsersUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DialogsAdapter extends RecyclerView.Adapter<DialogsAdapter.ViewHolder> {

    private List<DialogModel> mData;
    private DialogsContract.Presenter mPresenter;

    public DialogsAdapter(List<DialogModel> mData, DialogsContract.Presenter mPresenter) {
        this.mData = mData;
        this.mPresenter = mPresenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_dialog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DialogModel dialogModel = mData.get(position);

        holder.mChatName.setText(dialogModel.getName());
        holder.mChatDate.setText(DateUtils.millisecondsToDate(dialogModel.getLastMessageDateSent()));
        if (dialogModel.getLastMessageUserId() != null) {
            holder.mChatTitle.setText(UsersUtil.getUserById(dialogModel.getLastMessageUserId()).getFullName());

        }
        holder.mChatLastMessage.setText(dialogModel.getLastMessage());

        if (dialogModel.getUnreadMessagesCount() != 0) {
            holder.mNewMessageCounter.setVisibility(View.VISIBLE);
            holder.mChatName.setTextColor(Color.parseColor("#327780"));
            holder.mChatDate.setTextColor(Color.BLACK);
            holder.mNewMessageCounter.setText(String.valueOf(dialogModel.getUnreadMessagesCount()));
        }
        else {
            holder.mNewMessageCounter.setVisibility(View.GONE);
        }

        mPresenter.setImageDialog(holder.mDialogImage, dialogModel);


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

        public ViewHolder(View view) {
            super(view);
            mNewMessageCounter = (TextView) view.findViewById(R.id.recycler_chat_new_message_counter);
            mDialogImage = (CircleImageView) view.findViewById(R.id.recycler_chat_logo);
            mChatName = (TextView) view.findViewById(R.id.recycler_chat_name);
            mChatDate = (TextView) view.findViewById(R.id.recycler_chat_date);
            mChatTitle = (TextView) view.findViewById(R.id.recycler_chat_title);
            mChatLastMessage = (TextView) view.findViewById(R.id.recycler_chat_last_message);
        }
    }
}
