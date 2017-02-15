package com.example.lsdchat.unit.registration;

import com.example.lsdchat.ui.registration.RegistrationActivity;
import com.example.lsdchat.ui.registration.RegistrationContract;
import com.example.lsdchat.ui.registration.RegistrationPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationPresenterTest {
    private RegistrationContract.View view;
    private RegistrationContract.Model model;
    private RegistrationContract.Presenter presenter;

    @Before
    public void init() throws Exception {
        view = mock(RegistrationActivity.class);
        presenter = new RegistrationPresenter(view);
    }

    @Test
    public void errorMessageShouldBeShownOnWeakPasswordEntered() {
        String wrongPassword = "Qwwwwwww";
        presenter.validatePassword(wrongPassword);
        verify(view, times(1)).setWeakPasswordError();
    }

    @Test
    public void errorMessageShouldBeShownOnWrongLengthPasswordEntered() {
        String wrongPassword = "QQww33";
        presenter.validatePassword(wrongPassword);
        verify(view, times(1)).setLengthPasswordError();
    }

    @Test
    public void errorMessageShouldBeShownOnWrongEmailEntered() {
        String wrongEmail = "mkyong123@.";
        presenter.validateEmail(wrongEmail);
        verify(view, times(1)).setInvalideEmailError();
    }

    @Test
    public void errorMessageShouldBeShownOnDifferentPasswordsEntered() {
        String password = "QQwwee55";
        String confirmPassword = "QQwwee56";
        presenter.validateConfPassword(password, confirmPassword);
        verify(view, times(1)).setEquelsPasswordError();
    }

    @Test
    public void chooserDialogShouldBeShownOnUserAvatarClicked() {
        presenter.onAvatarClickListener();
        verify(view, times(1)).showDialogImageSourceChooser();
    }

    @Test
    public void invalidPasswordShouldFailValidation() {
        String[] invalidPasswords = {"", "Qwwwwwww", "Qwwwww55", "w55555555", "QQww55", "QQww55ww44rr99"};
        for (String s : invalidPasswords) {
            assertThat(presenter.validatePassword(s), is(false));
        }
    }

    @Test
    public void validPasswordShouldPassValidation() {
        String[] validPasswords = {"QQwwee55", "QwwEr3r3", "333RRvv22"};
        for (String s : validPasswords) {
            assertThat(presenter.validatePassword(s), is(true));
        }
    }

    @Test
    public void validEmailShouldPassValidation() {
        String[] validEmails = {"mkyong@yahoo.com",
                "mkyong-100@yahoo.com", "mkyong.100@yahoo.com",
                "mkyong111@mkyong.com", "mkyong-100@mkyong.net",
                "mkyong.100@mkyong.com.au", "mkyong@1.com",
                "mkyong@gmail.com.com", "mkyong+100@gmail.com",
                "mkyong-100@yahoo-test.com"};
        for (String s : validEmails) {
            assertThat(presenter.validateEmail(s), is(true));
        }
    }

    @Test
    public void invalidEmailShouldFailValidation() {
        String[] invalidEmails = {"mkyong", "mkyong@.com.my",
                "mkyong123@gmail.a", "mkyong123@.com", "mkyong123@.com.com",
                ".mkyong@mkyong.com", "mkyong()*@gmail.com", "mkyong@%*.com",
                "mkyong..2002@gmail.com", "mkyong.@gmail.com",
                "mkyong@mkyong@gmail.com", "mkyong@gmail.com.1a"};
        for (String s : invalidEmails) {
            assertThat(presenter.validateEmail(s), is(false));
        }
    }
}
