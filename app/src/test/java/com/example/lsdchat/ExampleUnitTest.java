package com.example.lsdchat;

import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.ui.login.LoginActivity;
import com.example.lsdchat.ui.login.LoginPresenter;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {


    @Mock
    LoginActivity loginActivity;
    @Mock
    DataManager dataManager;

    @InjectMocks
    LoginPresenter mLoginPresenter;



}