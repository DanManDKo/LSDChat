package com.example.lsdchat.ui.main.chats;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lsdchat.App;
import com.example.lsdchat.R;
import com.example.lsdchat.behavior.NonSwipeableViewPager;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.ui.login.LoginActivity;
import com.example.lsdchat.ui.main.NetworkConnect;
import com.example.lsdchat.ui.main.ViewPagerAdapter;
import com.example.lsdchat.ui.main.chats.dialogs.DialogsFragment;
import com.example.lsdchat.ui.main.createchat.CreateChatFragment;
import com.example.lsdchat.ui.main.fragment.BaseFragment;
import com.example.lsdchat.ui.main.users.UsersFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatsFragment extends BaseFragment implements ChatsContract.View {

    private FloatingActionButton mFloatingActionButton;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ChatsContract.Presenter mPresenter;
    private Toolbar mToolbar;
    private View mHeaderLayout;
    private CircleImageView mHeaderImage;
    private TextView mHeaderName;
    private TextView mHeaderEmail;

    private TabLayout mTabLayout;
    private NonSwipeableViewPager mViewPager;
    private TextView mSpannableText;
    private ImageView mNoChatsImage;
    private LinearLayout mNoChatsMessage;
    private SearchView mSearchView;
    private NetworkConnect networkConnect;

    public ChatsFragment() {
    }

    @Override
    public boolean isNetworkConnect() {
        return networkConnect.isNetworkConnect();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        networkConnect = ((NetworkConnect) getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        mPresenter = new ChatsPresenter(this, new ChatsModel(App.getSharedPreferencesManager(getActivity())));


        initView(view);
        initToolbar(mToolbar, "Chats");


        mFloatingActionButton.setOnClickListener(v -> navigateToNewChat());


        setNavigationItemSelectedListener();


        return view;
    }

    private void setNavigationItemSelectedListener() {
        mNavigationView.setNavigationItemSelectedListener(item -> {
            item.setChecked(true);
            switch (item.getItemId()) {
                case R.id.item_create_new_chat:
                    navigateToNewChat();
                    break;
                case R.id.item_users:
                    navigateToUsers();
                    break;
                case R.id.item_invite_users:
                    navigateToInviteUsers();
                    break;

                case R.id.item_settings:
                    navigateToSetting();
                    break;
                case R.id.item_log_out:
                    mPresenter.onLogout();
                    break;
            }
            mDrawerLayout.closeDrawers();
            return false;
        });
    }

    private void navigateToNewChat() {
        replaceFragment(new CreateChatFragment());
    }


    private void initView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.chats_toolbar);
        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.chats_fab);
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.chats_drawer);
        mNavigationView = (NavigationView) view.findViewById(R.id.chats_nav);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);

        mViewPager = (NonSwipeableViewPager) view.findViewById(R.id.chats_viewpager);
        mTabLayout = (TabLayout) view.findViewById(R.id.chats_tabs);
        mSpannableText = (TextView) view.findViewById(R.id.message_nochats_text_view);
        mNoChatsMessage = (LinearLayout) view.findViewById(R.id.message_nochats_root_layout);
        mNoChatsImage = (ImageView) view.findViewById(R.id.message_nochats_image_view);

        //set spannable text (underlined, clickable and colored)
        setSpannableText();


        initViewPagerAdapter(setFragmentList());


        /*
        for test toggleTabsVisibility - true
        delete later
       */
        toggleTabsVisibility(true);

        mHeaderLayout = mNavigationView.getHeaderView(0);
        mHeaderImage = (CircleImageView) mHeaderLayout.findViewById(R.id.nav_view_avatar);
        mHeaderName = (TextView) mHeaderLayout.findViewById(R.id.nav_view_full_name);
        mHeaderEmail = (TextView) mHeaderLayout.findViewById(R.id.nav_view_email_address);

        /*
        * set header data:
        * avatar, full name, email
        * */
        setHeaderImage();
        mHeaderName.setText(mPresenter.getUserModel().getFullName());
        mHeaderEmail.setText(mPresenter.getUserModel().getEmail());


//        Observable.from(UsersUtil.getAllUser())
//                .filter(user -> user.getBlobId()!=0)
//                .subscribe(user -> {
//                   Log.e("MY TETS",user.getImagePath());
//                });
    }


    private List<Fragment> setFragmentList() {
        List<Fragment> list = new ArrayList<>();
        list.add(new DialogsFragment().newInstance(ApiConstant.TYPE_DIALOG_PUBLIC));
        list.add(new DialogsFragment().newInstance(ApiConstant.TYPE_DIALOG_GROUP));

        return list;
    }

    private void setHeaderImage() {
        mPresenter.getUserAvatar().subscribe(path -> {
            if (path != null) {
                mHeaderImage.setImageURI(Uri.fromFile(new File(path)));
            } else {
                mHeaderImage.setImageResource(R.drawable.userpic);
            }
        });

    }

    private void initViewPagerAdapter(List<Fragment> fragmentList) {
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(new ViewPagerAdapter(getActivity().getSupportFragmentManager()
                , fragmentList, setFillTitleList()));
        mTabLayout.setupWithViewPager(mViewPager);

    }

    private List<String> setFillTitleList() {
        List<String> titleList = new ArrayList<>();
        titleList.add("Public");
        titleList.add("Private");
        return titleList;
    }


    private void toggleTabsVisibility(boolean value) {
        if (value) {
            mTabLayout.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.VISIBLE);
            mNoChatsMessage.setVisibility(View.GONE);
        } else {
            mTabLayout.setVisibility(View.GONE);
            mViewPager.setVisibility(View.GONE);
            mNoChatsMessage.setVisibility(View.VISIBLE);
        }
    }

    private void setSpannableText() {
        SpannableString spannableString = new SpannableString(getString(R.string.message_nochats_spannable_text_invite_friends));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                //TODO: onClick handling
                Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
            }
        };
        spannableString.setSpan(clickableSpan, 26, 40, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.colorNavyBlue)), 26, 40, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), 26, 40, 0);
        mSpannableText.setMovementMethod(LinkMovementMethod.getInstance());
        mSpannableText.setText(spannableString);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Log.e("TTT", "test");
                mDrawerLayout.openDrawer(Gravity.LEFT);
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void navigateToUsers() {
        replaceFragment(new UsersFragment());
    }

    @Override
    public void navigateToInviteUsers() {
        dialogError("kjndaskjhads");
    }

    @Override
    public void navigateToSetting() {

    }

    @Override
    public void navigateToLoginActivity() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

    private void replaceFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
    }

    @Override
    public void showMessageError(Throwable throwable) {
        dialogError(throwable);
    }


    @Override
    public boolean onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
            return true;
        } else {
            return false;

        }
    }


}