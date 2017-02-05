package com.example.lsdchat.unit.registration;

import com.example.lsdchat.ui.registration.RegistrationActivity;
import com.example.lsdchat.ui.registration.RegistrationContract;
import com.example.lsdchat.ui.registration.RegistrationPresenter;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

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

}
