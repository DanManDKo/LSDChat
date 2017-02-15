package com.example.lsdchat;

import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.lsdchat.ui.MainActivity;
import com.example.lsdchat.ui.login.LoginActivity;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class LoginUiTest {
    public static Context context;
    private static Instrumentation instr;
    private static Instrumentation.ActivityMonitor monitor;
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    @BeforeClass
    public static void init() {

        context = getInstrumentation().getTargetContext();
        instr = getInstrumentation();
        monitor = instr.addMonitor(LoginActivity.class.getName(), null, false);
    }

/*

    //    UI Test FR20
    @Test
    public void isButtonSignInDisable() {
        onView(withId(R.id.input_email)).perform(typeText("dddd@ddd.c"), closeSoftKeyboard());
        onView(withId(R.id.input_password)).perform(typeText("dddddddd"), closeSoftKeyboard());
        onView(withId(R.id.btn_sign_in)).perform(click());

        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            onView(withId(R.id.btn_sign_in)).check(matches(not(isEnabled())));
        }).start();
    }
*/


    @Test
    public void isSuccessfulGoToMainActivity() {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.example.lsdchat.ui",
                MainActivity.class.getName());
        intent.setComponent(componentName);
        Intents.init();
        onView(withId(R.id.input_email)).perform(typeText("aa@test.aa"), closeSoftKeyboard());
        onView(withId(R.id.input_password)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.btn_sign_in)).perform(click());


        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
//            onView(withId(R.id.textMain)).check(matches(isDisplayed()));
//            onView(withId(R.id.btn_sign_in)).check(matched());
//            intended(toPackage("com.example.lsdchat.ui.MainActivity"));
           /* Intent intent = new Intent();
            ComponentName componentName = new ComponentName("com.example.lsdchat.ui",
                    MainActivity.class.getName());
            intent.setComponent(componentName);
            Intents.init();
            InstrumentationRegistry.getContext().startActivity(intent);*/

        }).start();

    }



}
