package com.example.lsdchat.ui.main.createchat;


import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.lsdchat.R;
import com.example.lsdchat.model.ContactsModel;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CreateChatRvAdapter extends RecyclerView.Adapter<CreateChatRvAdapter.ViewHolder> {

    private List<ContactsModel> list;
    private Map<String, Boolean> mapChecked;
    private CreateChatContract.Presenter mPresenter;

    public CreateChatRvAdapter(CreateChatContract.Presenter presenter) {
        list = new ArrayList<>();
        this.mPresenter = presenter;

    }

    public void add(ContactsModel contactsModel) {
        list.add(contactsModel);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recyler_new_chat, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContactsModel user = list.get(position);
        holder.mName.setText(user.getName() + "/" + user.getEmail());

        if (user.getUri() != null) {
            Log.e("URI", user.getUri());
            holder.mImageView.setImageURI(Uri.fromFile(new File(user.getUri())));
        }

        mPresenter.setOnCheckedChangeListener(holder.mCheckBox,holder.mName,user);



    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView mImageView;
        TextView mName;
        CheckBox mCheckBox;


        ViewHolder(View itemView) {
            super(itemView);
            mImageView = (SimpleDraweeView) itemView.findViewById(R.id.new_chat_member_image);
            mName = (TextView) itemView.findViewById(R.id.new_chat_member_name);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.new_chat_member_checkbox);




        }


    }
}
