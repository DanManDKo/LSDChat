package com.example.lsdchat.ui.main.conversation;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lsdchat.R;
import com.example.lsdchat.api.dialog.model.ItemMessage;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class ConversationRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ConversationPresenter mConversationPresenter;
    private static final String SENDER_NAME = "Me";

    private List<ItemMessage> mList;
    private int userID;

    public ConversationRecyclerAdapter(List<ItemMessage> list, ConversationPresenter mConversationPresenter, int userID) {
        mList = list;
        this.userID = userID;
        this.mConversationPresenter = mConversationPresenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == userID) {
            view = inflater.inflate(R.layout.right_item_message_row, parent, false);
            return new OutcomingViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.left_item_message_row, parent, false);
            return new IncomingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemMessage itemMessage = mList.get(position);
        if (itemMessage != null) {

            if (itemMessage.getSender_id() == userID) {
                ((OutcomingViewHolder) holder).message.setText(mList.get(position).getMessage());
                String timeOut = mList.get(position).getCreatedAt().split("T")[1];
                ((OutcomingViewHolder) holder).time.setText(timeOut);
                ((OutcomingViewHolder) holder).personName.setText(SENDER_NAME);

                ((OutcomingViewHolder) holder).messageRoot.setOnClickListener(view -> {

                    mConversationPresenter.onAdapterItemClicked(mList.get(position).getId(), position);
                });
            } else {
                ((IncomingViewHolder) holder).message.setText(mList.get(position).getMessage());
                String timeIn = mList.get(position).getCreatedAt().split("T")[1];
                ((IncomingViewHolder) holder).time.setText(timeIn);
                ((IncomingViewHolder) holder).personName.setText("Sender");

                ((IncomingViewHolder) holder).messageRoot.setOnClickListener(view -> {

                    mConversationPresenter.onAdapterItemClicked(mList.get(position).getId(), position);
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mList != null) {
            ItemMessage itemMessage = mList.get(position);
            if (itemMessage != null) {
                return itemMessage.getSender_id();
            }
        }
        return 0;
    }

    class OutcomingViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout messageRoot;
        private TextView message;
        private TextView personName;
        private TextView time;
        private SimpleDraweeView image;

        public OutcomingViewHolder(View itemView) {
            super(itemView);
            messageRoot = (RelativeLayout) itemView.findViewById(R.id.bubble_layout);
            image = (SimpleDraweeView) itemView.findViewById(R.id.right_bubble_image);
            message = (TextView) itemView.findViewById(R.id.right_bubble_message);
            personName = (TextView) itemView.findViewById(R.id.right_bubble_person_name);
            time = (TextView) itemView.findViewById(R.id.right_bubble_time);
        }
    }

    class IncomingViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout messageRoot;
        private TextView message;
        private TextView personName;
        private TextView time;
        private SimpleDraweeView image;

        public IncomingViewHolder(View itemView) {
            super(itemView);
            messageRoot = (RelativeLayout) itemView.findViewById(R.id.bubble_layout);
            image = (SimpleDraweeView) itemView.findViewById(R.id.left_bubble_image);
            message = (TextView) itemView.findViewById(R.id.left_bubble_message);
            personName = (TextView) itemView.findViewById(R.id.left_bubble_person_name);
            time = (TextView) itemView.findViewById(R.id.left_bubble_time);
        }
    }

    public void addFirst(ItemMessage object) {
        mList.add(0, object);
        notifyDataSetChanged();
    }

    public void addAll(List<ItemMessage> list, int startIndex, int lastIndex) {
        if (list.size() != 0 && list.size() - 1 >= lastIndex) {
            List<ItemMessage> temp = list.subList(startIndex, lastIndex);
            Log.e("AAA", String.valueOf(temp.size()));
            mList.addAll(temp);
            notifyDataSetChanged();
        }
    }

    public void addMore(List<ItemMessage> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }
}
