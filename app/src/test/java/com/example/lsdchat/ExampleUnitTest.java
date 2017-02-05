package com.example.lsdchat;

import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.ui.login.LoginActivity;
import com.example.lsdchat.ui.login.LoginContract;
import com.example.lsdchat.ui.login.LoginPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    @Mock
    LoginActivity loginActivity;

    LoginContract.Presenter mLoginPresenter;

    @Before
    public void setUp() {
        mLoginPresenter = new LoginPresenter(loginActivity,new DataManager());
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void submitButtonTest() {

//       mLoginPresenter.
    }


}