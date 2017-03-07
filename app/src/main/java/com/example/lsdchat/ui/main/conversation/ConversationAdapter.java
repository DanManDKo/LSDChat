package com.example.lsdchat.ui.conversation;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lsdchat.R;
import com.example.lsdchat.api.dialog.model.ItemMessage;

import java.util.ArrayList;

public class ConversationAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    ArrayList<ItemMessage> chatMessageList;

    public ConversationAdapter(Activity activity, ArrayList<ItemMessage> list) {
        chatMessageList = list;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return chatMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemMessage message = chatMessageList.get(position);
        Integer senderId = message.getSender_id();
        View vi = convertView;
        if (convertView == null) vi = inflater.inflate(R.layout.chat_bubble_item, null);

        TextView msg = (TextView) vi.findViewById(R.id.bubble_message);
        TextView time = (TextView) vi.findViewById(R.id.bubble_time);
        RelativeLayout parentLayout = (RelativeLayout) vi.findViewById(R.id.bubble_layout_parent);
        RelativeLayout innerLayout = (RelativeLayout) vi.findViewById(R.id.bubble_layout);
        LinearLayout detailsLayout = (LinearLayout) vi.findViewById(R.id.bubble_details_layout);

//        SimpleDraweeView image = (SimpleDraweeView) vi.findViewById(R.id.bubble_image);
//        RelativeLayout.LayoutParams paramsImage = (RelativeLayout.LayoutParams) image.getLayoutParams();
//        RelativeLayout.LayoutParams paramsRoot = (RelativeLayout.LayoutParams) innerLayout.getLayoutParams();

        // if message is mine then align to right
        Integer userId = 23163511;
        if (senderId.equals(userId)) {
//            paramsImage.addRule(RelativeLayout.ALIGN_PARENT_END);
//            paramsRoot.addRule(RelativeLayout.LEFT_OF, R.id.bubble_image);
            innerLayout.setBackgroundResource(R.drawable.right_bubble);
            parentLayout.setGravity(Gravity.RIGHT);
            msg.setPadding(16, 16, 32, 16);
            detailsLayout.setPadding(16, 0, 32, 0);
        }
        // If not mine then align to left
        else {
            innerLayout.setBackgroundResource(R.drawable.left_bubble);
//            paramsImage.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//            paramsImage.setMargins(0, 0, 16, 0);
//            paramsRoot.addRule(RelativeLayout.RIGHT_OF, R.id.bubble_image);
            parentLayout.setGravity(Gravity.LEFT);
            msg.setPadding(32, 16, 16, 16);
            detailsLayout.setPadding(32, 0, 16, 0);
        }
        msg.setText(message.getMessage());
        time.setText(message.getCreatedAt().split("T")[0]);
        innerLayout.setOnClickListener(view -> {
            Toast.makeText(convertView.getContext(), "message", Toast.LENGTH_SHORT).show();
        });
        return vi;
    }

    public void add(ItemMessage object) {
        chatMessageList.add(object);
    }
}
