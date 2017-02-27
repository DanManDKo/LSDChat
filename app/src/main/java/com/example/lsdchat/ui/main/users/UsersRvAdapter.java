package com.example.lsdchat.ui.main.users;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lsdchat.R;
import com.example.lsdchat.model.UserQuick;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;

import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

 
public class UsersRvAdapter extends RealmBasedRecyclerViewAdapter<UserQuick,UsersRvAdapter.ViewHolder> {


    public UsersRvAdapter(Context context, RealmResults<UserQuick> realmResults, boolean automaticUpdate, boolean animateResults) {
        super(context, realmResults, automaticUpdate, animateResults);
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_recycler_users, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int i) {
        UserQuick userQuick = realmResults.get(i);
        if (userQuick.getImagePath()!=null) {
            viewHolder.mImageView.setImageURI(Uri.fromFile(new File(userQuick.getImagePath())));
        }

        viewHolder.mName.setText(userQuick.getFullName());
    }





    public class ViewHolder extends RealmViewHolder {

        SimpleDraweeView mImageView;
        TextView mName;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (SimpleDraweeView) itemView.findViewById(R.id.users_image);
            mName = (TextView) itemView.findViewById(R.id.users_name);

        }
    }
}
