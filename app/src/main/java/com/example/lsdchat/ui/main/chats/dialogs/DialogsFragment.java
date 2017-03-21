package com.example.lsdchat.ui.main.chats.dialogs;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lsdchat.App;
import com.example.lsdchat.R;
import com.example.lsdchat.model.ContentModel;
import com.example.lsdchat.model.RealmDialogModel;
import com.example.lsdchat.ui.main.fragment.BaseFragment;

import java.util.List;


public class DialogsFragment extends BaseFragment implements DialogsContract.View {

    private static final String TYPE = "type";

    private DialogsContract.Presenter mPresenter;
    private int mType;
    private RecyclerView mRecyclerView;
    private DialogsAdapter mDialogsAdapter;
    private List<RealmDialogModel> mList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Toolbar mToolbar;


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
    }




    @Override
    public int getType() {
        return mType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialogs, container, false);
        mPresenter = new DialogsPresenter(this, new DialogsModel(App.getSharedPreferencesManager(getActivity())));
        mType = getArguments().getInt(TYPE);
        initView(view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mDialogsAdapter = new DialogsAdapter(mPresenter);
        mRecyclerView.setAdapter(mDialogsAdapter);


        mPresenter.getObservableDialogByType(mType);

        mPresenter.getContentModelList();

        setRefreshLayout();

        initSwipeDelete();


        return view;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.chats_recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);



    }

    @Override
    public void showErrorDialog(Throwable throwable) {
        showErrorDialog(throwable);
    }

    @Override
    public void setContentModelList(List<ContentModel> contentModelList){
        mDialogsAdapter.setContentModelList(contentModelList);
    }

    private void setRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mPresenter.getAllDialogAndSave();

            mPresenter.getObservableDialogByType(mType);
            mSwipeRefreshLayout.setRefreshing(false);
        });
    }


    @Override
    public void setListDialog(List<RealmDialogModel> list) {
        clearListDialog();
        mDialogsAdapter.addData(list);
    }

    private void clearListDialog() {
        mDialogsAdapter.clearData();
    }

    @Override
    public void deleteItemDialog(RealmDialogModel item) {
        mDialogsAdapter.deleteItemData(item);
        mDialogsAdapter.notifyDataSetChanged();
    }

    private void initSwipeDelete() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            int xMarkMargin;
            Drawable xMark;
            boolean initiated;

            private void init() {
                xMark = ContextCompat.getDrawable(getActivity(), R.drawable.delete);
                xMarkMargin = (int) getActivity().getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.alert_delete_chat);

                builder.setPositiveButton(R.string.alert_delete_chat_delete, (dialog, which) -> {
//                    TODO: add delete dialog logic
                    setListDialog(mList);
                    mDialogsAdapter.notifyItemRemoved(viewHolder.getLayoutPosition());

                }).setNegativeButton(R.string.alert_delete_chat_cancel, (dialog, which) -> {
                    mDialogsAdapter.notifyDataSetChanged();
                    dialog.dismiss();

                }).setCancelable(false).show();
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;


                if (!initiated) {
                    init();
                }
                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);


                //Setting Swipe Text
                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#327780"));
                paint.setTextSize(40);
                paint.setTextAlign(Paint.Align.CENTER);
                c.drawText("Delete", xMarkLeft + 20, xMarkTop + 80, paint);


                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }



    @Override
    public void navigateToChat(Fragment fragment) {
//        TODO: add animation
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).addToBackStack(null).commit();
    }
}
