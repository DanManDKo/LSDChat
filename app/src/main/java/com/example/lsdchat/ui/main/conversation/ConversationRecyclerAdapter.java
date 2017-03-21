package com.example.lsdchat.ui.main.conversation;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lsdchat.R;
import com.example.lsdchat.api.dialog.model.ItemMessage;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.model.ContentModel;
import com.example.lsdchat.util.DateUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversationRecyclerAdapter extends RecyclerView.Adapter<ConversationRecyclerAdapter.LocalViewHolder> {
    private ConversationPresenter mConversationPresenter;
    private static final String SENDER_NAME = "Me";
    private static final int OUTCOMING_MSG = 0;
    private static final int INCOMING_MSG = 1;
    private static final int DATE_SEPARATOR = 2;

    private List<ItemMessage> mList;
    private List<LoginUser> mUsers;
    private List<ContentModel> mContentModels;

    private Map<String, String> mMapAvatar;
    private Map<Integer, String> mMapName;
    private int userID;

    public ConversationRecyclerAdapter(List<ItemMessage> list, ConversationPresenter mConversationPresenter, int userID) {
        mList = list;
        mUsers = new ArrayList<>();
        mContentModels = new ArrayList<>();
        mMapAvatar = new HashMap<>();
        mMapName = new HashMap<>();
        this.userID = userID;
        this.mConversationPresenter = mConversationPresenter;
    }

    @Override
    public ConversationRecyclerAdapter.LocalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        switch (viewType) {
            case OUTCOMING_MSG:
                view = inflater.inflate(R.layout.right_item_message_row, parent, false);
                return new LocalViewHolder(view, viewType);
            case INCOMING_MSG:
                view = inflater.inflate(R.layout.left_item_message_row, parent, false);
                return new LocalViewHolder(view, viewType);
            default:
                view = inflater.inflate(R.layout.header_item_message_row, parent, false);
                return new LocalViewHolder(view, viewType);
        }
    }

    @Override
    public void onBindViewHolder(ConversationRecyclerAdapter.LocalViewHolder holder, int position) {
        ItemMessage itemMessage = mList.get(position);
        holder.itemView.setTag(itemMessage);

        if (itemMessage != null) {
            holder.message.setText(mList.get(position).getMessage());

            if (itemMessage.getSender_id() == userID) {
                holder.personName.setText(SENDER_NAME);
            } else {
                String person = mMapName.get(itemMessage.getSender_id());
                holder.personName.setText(person);
            }

            String time = mList.get(position).getDateSent();
            holder.time.setText(DateUtils.millisecondsToDate(time));

            String path = mMapAvatar.get(String.valueOf((itemMessage.getSender_id())));
            if (path != null) {
                holder.image.setImageURI(Uri.fromFile(new File(path)));
            } else {
                holder.image.setImageResource(R.drawable.userpic);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        ItemMessage itemMessage = mList.get(position);

        if (itemMessage.getSender_id() == userID) {
            return OUTCOMING_MSG;
        } else return INCOMING_MSG;
    }

    class LocalViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout messageRoot;
        private TextView message;
        private TextView personName;
        private TextView time;
        private TextView headerTime;
        private CircleImageView image;

        public LocalViewHolder(View itemView, int viewType) {
            super(itemView);
            switch (viewType) {
                case OUTCOMING_MSG:
                case INCOMING_MSG:
                    messageRoot = (RelativeLayout) itemView.findViewById(R.id.bubble_layout);
                    image = (CircleImageView) itemView.findViewById(R.id.right_bubble_image);
                    message = (TextView) itemView.findViewById(R.id.right_bubble_message);
                    personName = (TextView) itemView.findViewById(R.id.right_bubble_person_name);
                    time = (TextView) itemView.findViewById(R.id.right_bubble_time);
                    break;
                case DATE_SEPARATOR:
                    headerTime = (TextView) itemView.findViewById(R.id.header_message_recycler);
                    break;
                default:
                    break;
            }
            itemView.setOnLongClickListener(v -> {
                ItemMessage item = (ItemMessage) v.getTag();
                int position = mList.indexOf(item);
                mConversationPresenter.onAdapterItemClicked(mList.get(position).getId(), position, mList.get(position).getMessage(), mList.get(position).getChatDialogId());
                return true;
            });
        }
    }

    public void addFirst(ItemMessage object) {
        mList.add(0, object);
        notifyItemInserted(0);
    }

    public void addAll(List<ItemMessage> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addMore(List<ItemMessage> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addUsersList(List<LoginUser> users) {
        mUsers.addAll(users);
        for (LoginUser o : mUsers) {
            mMapName.put(o.getId(), o.getFullName());
        }
    }

    public void addContentList(List<ContentModel> content) {
        mContentModels.addAll(content);
        for (ContentModel o : mContentModels) {
            mMapAvatar.put(o.getId(), o.getImagePath());
        }
    }

    public void deleteItem(int position) {
        mUsers.remove(position);
        notifyItemRemoved(position);
    }

    public void updateItem(int position, String message) {
        mList.get(position).setMessage(message);
        notifyItemChanged(position);
    }
}
