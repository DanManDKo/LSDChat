package com.example.lsdchat.ui.main.dialogs;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lsdchat.R;
import com.example.lsdchat.api.dialog.model.ItemDialog;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import io.realm.RealmViewHolder;

/**
 * Created by User on 26.02.2017.
 */

public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.ViewHolder> {
    private List<ItemDialog> mDialogs;

    public DialogAdapter(@NonNull List<ItemDialog> dialogs) {
        mDialogs = dialogs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_dialog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemDialog itemDialog = mDialogs.get(position);
        holder.mNewMessagesCount.setText(itemDialog.getUnreadMessagesCount().toString());
        holder.mChatName.setText(itemDialog.getName());
        holder.mChatDate.setText(itemDialog.getCreatedAt());
        holder.mLastMessage.setText(itemDialog.getLastMessage());
    }

    @Override
    public int getItemCount() {
        return mDialogs.size();
    }

    public class ViewHolder extends RealmViewHolder {
        private TextView mChatName;
        private TextView mChatDate;
        private TextView mChatTitle;
        private TextView mLastMessage;
        private TextView mNewMessagesCount;
        private SimpleDraweeView mChatLogo;

        public ViewHolder(View view) {
            super(view);
            mChatName = (TextView) view.findViewById(R.id.recycler_chat_name);
            mChatDate = (TextView) view.findViewById(R.id.recycler_chat_date);
            mChatTitle = (TextView) view.findViewById(R.id.recycler_chat_title);
            mLastMessage = (TextView) view.findViewById(R.id.recycler_chat_last_message);
            mNewMessagesCount = (TextView) view.findViewById(R.id.recycler_chat_new_message_counter);
            mChatLogo = (SimpleDraweeView) view.findViewById(R.id.recycler_chat_logo);
        }
    }
}
