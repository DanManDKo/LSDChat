package com.example.lsdchat.ui.login;

import com.example.lsdchat.manager.DataManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.PowerMockito.mock;


@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {
    private LoginContract.View view;
    private LoginContract.Presenter presenter;
    private DataManager dataManager;

    @Before
    public void setUp() throws Exception {
        view = mock(LoginActivity.class);
        presenter = new LoginPresenter(view,dataManager);
    }

    @Test
    public void passCorrectTest() {
        assertThat(presenter.isValidPassword("aaaaaaaa"), is(true));
    }





}