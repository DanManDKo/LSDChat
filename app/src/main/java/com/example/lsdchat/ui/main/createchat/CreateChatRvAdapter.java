package com.example.lsdchat.ui.main.createchat;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.lsdchat.R;
import com.example.lsdchat.model.ContactsModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class CreateChatRvAdapter extends RecyclerView.Adapter<CreateChatRvAdapter.ViewHolder> {

    private List<ContactsModel> mList;
    private CreateChatContract.Presenter mPresenter;

    public CreateChatRvAdapter(List<ContactsModel> mList, CreateChatContract.Presenter presenter) {
        this.mList = mList;
        this.mPresenter = presenter;

    }

    public void add(ContactsModel contactsModel) {
        mList.add(contactsModel);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recyler_new_chat, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContactsModel user = mList.get(position);
        holder.mName.setText(user.getName());


        mPresenter.setImageViewUser(holder.mImageView,user);
        mPresenter.setOnCheckedChangeListener(holder.mCheckBox,holder.mName,user);



    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView mImageView;
        TextView mName;
        CheckBox mCheckBox;


        ViewHolder(View itemView) {
            super(itemView);
            mImageView = (CircleImageView) itemView.findViewById(R.id.new_chat_member_image);
            mName = (TextView) itemView.findViewById(R.id.new_chat_member_name);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.new_chat_member_checkbox);




        }


    }
}
