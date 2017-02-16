package com.example.lsdchat.ui.conversation;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lsdchat.R;
import com.example.lsdchat.api.dialog.model.ItemMessage;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class ConversationRecyclerAdapter extends RecyclerView.Adapter<ConversationRecyclerAdapter.ViewHolder> {
    private List<ItemMessage> mList;
    private OnRecyclerItemClickListener mListener;

    public void setListener(OnRecyclerItemClickListener listener) {
        mListener = listener;
    }

    public ConversationRecyclerAdapter(List<ItemMessage> list) {
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.chat_bubble_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position % 2 == 0) {
            holder.innerLayout.setBackgroundResource(R.drawable.right_bubble);
            holder.paramsImage.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.paramsImage.setMargins(16, 0, 0, 0);
            holder.paramsRoot.addRule(RelativeLayout.LEFT_OF, R.id.bubble_image);
            holder.parentLayout.setGravity(Gravity.RIGHT);
            holder.message.setPadding(16, 16, 32, 16);
            holder.detailsLayout.setPadding(16, 0, 32, 0);
        } else {
            holder.innerLayout.setBackgroundResource(R.drawable.left_bubble);
            holder.paramsImage.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.paramsImage.setMargins(0, 0, 16, 0);
            holder.paramsRoot.addRule(RelativeLayout.RIGHT_OF, R.id.bubble_image);
            holder.parentLayout.setGravity(Gravity.LEFT);
            holder.message.setPadding(32, 16, 16, 16);
            holder.detailsLayout.setPadding(32, 0, 16, 0);
        }

        holder.message.setText(mList.get(position).getMessage());
        holder.message.setOnClickListener(view -> {
            //there we can change bubble color and edit message
            mListener.onItemClicked(mList.get(position).getId());
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout.LayoutParams paramsImage;
        private RelativeLayout.LayoutParams paramsRoot;
        private LinearLayout detailsLayout;
        private RelativeLayout parentLayout;
        private RelativeLayout innerLayout;
        private TextView message;
        private SimpleDraweeView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (SimpleDraweeView) itemView.findViewById(R.id.bubble_image);
            message = (TextView) itemView.findViewById(R.id.bubble_message);
            parentLayout = (RelativeLayout) itemView.findViewById(R.id.bubble_layout_parent);
            innerLayout = (RelativeLayout) itemView.findViewById(R.id.bubble_layout);
            detailsLayout = (LinearLayout) itemView.findViewById(R.id.bubble_details_layout);

            paramsImage = (RelativeLayout.LayoutParams) image.getLayoutParams();
            paramsRoot = (RelativeLayout.LayoutParams) innerLayout.getLayoutParams();
        }
    }

    public interface OnRecyclerItemClickListener {
        void onItemClicked(String id);
    }
}
