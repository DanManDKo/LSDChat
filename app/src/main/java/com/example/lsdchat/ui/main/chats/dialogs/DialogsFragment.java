package com.example.lsdchat.ui.main.chats.dialogs;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lsdchat.R;
import com.example.lsdchat.ui.main.fragment.BaseFragment;


public class DialogsFragment extends BaseFragment implements DialogsContract.View {

    private static final String TYPE = "type";

    private DialogsContract.Presenter mPresenter;
    private int mType;
    private RecyclerView mRecyclerView;



    public DialogsFragment() {
        // Required empty public constructor
    }


    public DialogsFragment newInstance(int type) {
        DialogsFragment fragment = new DialogsFragment();
        Bundle args = new Bundle();
        args.putInt(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialogs, container, false);
        mPresenter = new DialogsPresenter(this);
        mType = getArguments().getInt(TYPE);

        mPresenter.showDialogs(mType);



        return view;
    }


}
