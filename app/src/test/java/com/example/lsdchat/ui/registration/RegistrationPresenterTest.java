package com.example.lsdchat.ui.registration;

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
