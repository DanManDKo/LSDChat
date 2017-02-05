package com.example.lsdchat.ui.registration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationPresenterTest {
    private RegistrationContract.View view;
    private RegistrationContract.Presenter presenter;

    @Before
    public void init() throws Exception {
        view = mock(RegistrationActivity.class);
        presenter = new RegistrationPresenter(view);
    }

    @Test
    public void invalidPasswordShoudFailValidation() {
        String[] invalidPasswords = {"", "Qwwwwwww", "Qwwwww55", "w55555555", "QQww55", "QQww55ww44rr99"};
        for (String s: invalidPasswords) {
            assertThat(presenter.validatePassword(s),is(false));
        }
    }

    @Test
    public void validPasswordShoudPassValidation() {
        String[] validPasswords = {"QQwwee55", "QwwEr3r3", "333RRvv22"};
        for (String s: validPasswords) {
            assertThat(presenter.validatePassword(s),is(true));
        }
    }

    @Test
    public void validEmailShoudPassValidation() {
        String[] validEmails = {"mkyong@yahoo.com",
                "mkyong-100@yahoo.com", "mkyong.100@yahoo.com",
                "mkyong111@mkyong.com", "mkyong-100@mkyong.net",
                "mkyong.100@mkyong.com.au", "mkyong@1.com",
                "mkyong@gmail.com.com", "mkyong+100@gmail.com",
                "mkyong-100@yahoo-test.com"};
        for (String s: validEmails) {
            assertThat(presenter.validateEmail(s),is(true));
        }
    }

    @Test
    public void invalidEmailShoudFailValidation() {
        String[] invalidEmails = {"mkyong", "mkyong@.com.my",
                "mkyong123@gmail.a", "mkyong123@.com", "mkyong123@.com.com",
                ".mkyong@mkyong.com", "mkyong()*@gmail.com", "mkyong@%*.com",
                "mkyong..2002@gmail.com", "mkyong.@gmail.com",
                "mkyong@mkyong@gmail.com", "mkyong@gmail.com.1a"};
        for (String s: invalidEmails) {
            assertThat(presenter.validateEmail(s),is(false));
        }
    }

}
