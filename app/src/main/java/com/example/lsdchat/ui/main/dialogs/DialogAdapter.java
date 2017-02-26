package com.example.lsdchat.ui.main.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lsdchat.R;
import com.example.lsdchat.api.dialog.model.ItemDialog;

import java.util.List;

import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by User on 26.02.2017.
 */

public class DialogAdapter extends RealmBasedRecyclerViewAdapter<ItemDialog,DialogAdapter.ViewHolder> {


    public DialogAdapter(Context context, RealmResults<ItemDialog> realmResults, boolean automaticUpdate, boolean animateResults, String animateExtraColumnName) {
        super(context, realmResults, automaticUpdate, animateResults, animateExtraColumnName);
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        View view = View.inflate(R.layout.)
        return null;
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int i) {

    }

    public class ViewHolder extends RealmViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
