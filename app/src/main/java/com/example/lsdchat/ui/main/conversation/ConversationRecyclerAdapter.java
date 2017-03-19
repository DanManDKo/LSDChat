package com.example.lsdchat.ui.main.conversation;

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
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversationRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ConversationPresenter mConversationPresenter;
    private static final String SENDER_NAME = "Me";

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

                String path = mMapAvatar.get(String.valueOf((itemMessage.getSender_id())));
                if (path != null) {
                    ((OutcomingViewHolder) holder).image.setImageURI(Uri.fromFile(new File(path)));
                } else {
                    ((OutcomingViewHolder) holder).image.setImageResource(R.drawable.userpic);
                }

                ((OutcomingViewHolder) holder).messageRoot.setOnClickListener(view -> {

                    mConversationPresenter.onAdapterItemClicked(mList.get(position).getId(), position);
                });
            } else {
                ((IncomingViewHolder) holder).message.setText(mList.get(position).getMessage());

//                String timeIn = mList.get(position).getCreatedAt().split("T")[1];
                String time = mList.get(position).getDateSent();
                ((IncomingViewHolder) holder).time.setText(DateUtils.millisecondsToDate(time));

                String person = mMapName.get(itemMessage.getSender_id());
                ((IncomingViewHolder) holder).personName.setText(person);

                String path = mMapAvatar.get(String.valueOf((itemMessage.getSender_id())));
                if (path != null) {
                    ((IncomingViewHolder) holder).image.setImageURI(Uri.fromFile(new File(path)));
                } else {
                    ((IncomingViewHolder) holder).image.setImageResource(R.drawable.userpic);
                }

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
        private CircleImageView image;

        public OutcomingViewHolder(View itemView) {
            super(itemView);
            messageRoot = (RelativeLayout) itemView.findViewById(R.id.bubble_layout);
            image = (CircleImageView) itemView.findViewById(R.id.right_bubble_image);
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
        private CircleImageView image;

        public IncomingViewHolder(View itemView) {
            super(itemView);
            messageRoot = (RelativeLayout) itemView.findViewById(R.id.bubble_layout);
            image = (CircleImageView) itemView.findViewById(R.id.left_bubble_image);
            message = (TextView) itemView.findViewById(R.id.left_bubble_message);
            personName = (TextView) itemView.findViewById(R.id.left_bubble_person_name);
            time = (TextView) itemView.findViewById(R.id.left_bubble_time);
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
        for (ContentModel o: mContentModels) {
            mMapAvatar.put(o.getId(), o.getImagePath());
        }
    }
}
