package jp.caliconography.miminenreichecker.app;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import jp.caliconography.android.miminenreichecker.R;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.*;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.*;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.*;

/**
 * Created by abeharuhiko on 2014/07/16.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private static final String TAG = MainActivityTest.class.getSimpleName();
    private Activity mActivity;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
    }

    public void testかんたんチェックの全ボタンが有効() {
        // 画面内にボタンが表示されていることと初期状態でボタンが押せる（Enabled）ことを確認。
        onView(withId(R.id.btn_lv1)).check(
                matches(isCompletelyDisplayed())).check(
                matches(isEnabled()));

        onView(withId(R.id.btn_lv2)).check(
                matches(isCompletelyDisplayed())).check(
                matches(isEnabled()));

        onView(withId(R.id.btn_lv3)).check(
                matches(isCompletelyDisplayed())).check(
                matches(isEnabled()));

        onView(withId(R.id.btn_lv4)).check(
                matches(isCompletelyDisplayed())).check(
                matches(isEnabled()));

        onView(withId(R.id.btn_lv5)).check(
                matches(isCompletelyDisplayed())).check(
                matches(isEnabled()));

        onView(withId(R.id.btn_lv6)).check(
                matches(isCompletelyDisplayed())).check(
                matches(isEnabled()));

    }
}
