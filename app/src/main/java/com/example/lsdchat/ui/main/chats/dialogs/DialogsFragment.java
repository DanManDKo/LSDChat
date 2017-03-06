package com.example.lsdchat.ui.main.chats.dialogs;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lsdchat.App;
import com.example.lsdchat.R;
import com.example.lsdchat.model.DialogModel;
import com.example.lsdchat.ui.main.fragment.BaseFragment;

import java.util.List;


public class DialogsFragment extends BaseFragment implements DialogsContract.View {

    private static final String TYPE = "type";

    private DialogsContract.Presenter mPresenter;
    private int mType;
    private RecyclerView mRecyclerView;
    private DialogsAdapter mDialogsAdapter;
    private List<DialogModel> mList;
    private SwipeRefreshLayout mSwipeRefreshLayout;



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
    public int getType() {
        return mType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialogs, container, false);
        mPresenter = new DialogsPresenter(this, App.getSharedPreferencesManager(getActivity()));
        mType = getArguments().getInt(TYPE);
        initView(view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mList = mPresenter.showDialogs(mType);
        initAdapter(mList);
        mPresenter.setOnRefreshListener(mSwipeRefreshLayout);

        return view;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.chats_recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

    }

    @Override
    public void initAdapter(List<DialogModel> list) {
        mDialogsAdapter = new DialogsAdapter(list,mPresenter);
        mRecyclerView.setAdapter(mDialogsAdapter);
        mDialogsAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateAdapter() {
        mDialogsAdapter.notifyDataSetChanged();
    }
}
