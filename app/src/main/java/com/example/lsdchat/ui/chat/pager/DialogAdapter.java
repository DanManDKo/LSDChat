package com.example.lsdchat.ui.chat.pager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lsdchat.R;
import com.example.lsdchat.api.dialog.model.ItemDialog;
import com.example.lsdchat.manager.model.RealmItemDialog;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by User on 19.02.2017.
 */

public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.ViewHolder> {
    private List<RealmItemDialog> mItems;

    public DialogAdapter(@NonNull List<RealmItemDialog> items) {
        mItems = items;
    }



    public void addItems(List<RealmItemDialog> items) {
        if (mItems != null)
            mItems.addAll(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_main_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RealmItemDialog itemDialog = mItems.get(position);
        holder.mNewMessageCounter.setText(itemDialog.getUnreadMessagesCount().toString());
//        holder.mDialogImage.setImageURI(itemDialog.get);
        holder.mChatName.setText(itemDialog.getName());
        holder.mChatDate.setText(itemDialog.getCreatedAt());
//        holder.mChatTitle.setText(itemDialog.get());
        holder.mChatLastMessage.setText(itemDialog.getLastMessage());

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mNewMessageCounter;
        private SimpleDraweeView mDialogImage;
        private TextView mChatName;
        private TextView mChatDate;
        private TextView mChatTitle;
        private TextView mChatLastMessage;

        public ViewHolder(View view) {
            super(view);
            mNewMessageCounter = (TextView) view.findViewById(R.id.recycler_new_message_counter);
            mDialogImage = (SimpleDraweeView) view.findViewById(R.id.recycler_chat_logo);
            mChatName = (TextView) view.findViewById(R.id.recycler_chat_name);
            mChatDate = (TextView) view.findViewById(R.id.recycler_chat_date);
            mChatTitle = (TextView) view.findViewById(R.id.recycler_chat_title);
            mChatLastMessage = (TextView) view.findViewById(R.id.recycler_chat_lastmessage);
        }
    }
}
