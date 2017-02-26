package com.example.lsdchat.ui.conversation;

import android.support.v7.widget.RecyclerView;
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
    private static final String SENDER_NAME = "Me";

    private List<ItemMessage> mList;
    private OnRecyclerItemClickListener mListener;

    public void setListener(OnRecyclerItemClickListener listener) {
        mListener = listener;
    }

    public ConversationRecyclerAdapter(List<ItemMessage> list) {
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        switch (viewType) {
            case ItemMessage.OUTCOMING_MESSAGE:
                view = inflater.inflate(R.layout.right_item_message_row, parent, false);
                return new OutcomingViewHolder(view);
            case ItemMessage.INCOMING_MESSAGE:
                view = inflater.inflate(R.layout.left_item_message_row, parent, false);
                return new IncomingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemMessage itemMessage = mList.get(position);
        if (itemMessage != null) {
            switch (itemMessage.getType()) {
                case ItemMessage.OUTCOMING_MESSAGE:
                    ((OutcomingViewHolder) holder).message.setText(mList.get(position).getMessage());
                    ((OutcomingViewHolder) holder).time.setText(mList.get(position).getDateSent());
                    ((OutcomingViewHolder) holder).personName.setText(SENDER_NAME);

                    ((OutcomingViewHolder) holder).messageRoot.setOnClickListener(view -> {

                        mListener.onItemClicked(mList.get(position).getId(), position, ItemMessage.OUTCOMING_MESSAGE);
                    });
                    break;
                case ItemMessage.INCOMING_MESSAGE:
                    ((IncomingViewHolder) holder).message.setText(mList.get(position).getMessage());
                    ((IncomingViewHolder) holder).time.setText(mList.get(position).getDateSent());
                    ((IncomingViewHolder) holder).personName.setText("Sender");

                    ((IncomingViewHolder) holder).messageRoot.setOnClickListener(view -> {
                        mListener.onItemClicked(mList.get(position).getId(), position, ItemMessage.INCOMING_MESSAGE);
                    });
                    break;
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
                return itemMessage.getType();
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

    public void add(ItemMessage object) {
        mList.add(object);
    }

    public interface OnRecyclerItemClickListener {
        void onItemClicked(String id, int position, int type);
    }
}
