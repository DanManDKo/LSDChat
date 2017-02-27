package com.example.lsdchat.ui.main.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lsdchat.R;

/**
 * Created by User on 27.02.2017.
 */

public class DialogListFragment extends Fragment {
    static final String DIALOG_TYPE_KEY = "typeKey";
    static final int PUBLIC_DIALOG = 1;
    static final int PRIVATE_DIALOG = 2;

    public static DialogListFragment getInstance(int dialogType) {
        DialogListFragment dialogFragment = new DialogListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DIALOG_TYPE_KEY, dialogType);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_dialog_list,container,false);
        initView(view);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initView(View view) {

    }
}
