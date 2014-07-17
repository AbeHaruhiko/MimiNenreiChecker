package jp.caliconography.miminenreichecker.app;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.google.android.apps.common.testing.ui.espresso.contrib.CountingIdlingResource;

import jp.caliconography.android.miminenreichecker.R;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.scrollTo;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.swipeLeft;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.swipeRight;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.hasSibling;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isEnabled;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

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
        onView(withId(R.id.btn_lv1))
                .check(matches(isEnabled()));

        onView(withId(R.id.btn_lv2))
                .check(matches(isEnabled()));

        onView(withId(R.id.btn_lv3))
                .check(matches(isEnabled()));

        onView(withId(R.id.btn_lv4))
                .check(matches(isEnabled()));

        onView(withId(R.id.btn_lv5))
                .check(matches(isEnabled()));

        onView(withId(R.id.btn_lv6))
                .check(matches(isEnabled()));

    }

    public void testLv1ボタンClickで他のLvボタンが無効になる() {

        onView(withId(R.id.btn_lv1))
                .perform(click());

        // 有効である
        onView(withId(R.id.btn_lv1))
                .check(matches(isEnabled()));

        // 無効である
        onView(withId(R.id.btn_lv2))
                .check(matches(not(isEnabled())));

        onView(withId(R.id.btn_lv3)).check(
                matches(not(isEnabled())));

        onView(withId(R.id.btn_lv4)).check(
                matches(not(isEnabled())));

        onView(withId(R.id.btn_lv5)).check(
                matches(not(isEnabled())));

        onView(withId(R.id.btn_lv6)).check(
                matches(not(isEnabled())));

        // 右アイコンがfa-playでない
        onView(
                allOf(
                        withId(R.id.lblRight)
                        , hasSibling(withText(R.string.lbl_lv1))
                )
        )
                .check(matches(not(withText(R.string.fa_play))));

        // 右アイコンがfa-playである
        onView(
                allOf(
                        withId(R.id.lblRight)
                        , hasSibling(withText(R.string.lbl_lv2))
                )
        )
                .check(matches(withText(R.string.fa_play)));

        onView(
                allOf(
                        withId(R.id.lblRight)
                        , hasSibling(withText(R.string.lbl_lv3))
                )
        )
                .check(matches(withText(R.string.fa_play)));

        onView(
                allOf(
                        withId(R.id.lblRight)
                        , hasSibling(withText(R.string.lbl_lv4))
                )
        )
                .check(matches(withText(R.string.fa_play)));

        onView(
                allOf(
                        withId(R.id.lblRight)
                        , hasSibling(withText(R.string.lbl_lv5))
                )
        )
                .check(matches(withText(R.string.fa_play)));

        onView(
                allOf(
                        withId(R.id.lblRight)
                        , hasSibling(withText(R.string.lbl_lv6))
                )
        )
                .check(matches(withText(R.string.fa_play)));


    }

    public void testLv2ボタンClickで他のLvボタンが無効になる() {

        onView(withId(R.id.btn_lv2))
                .perform(click());

        // 有効である
        onView(withId(R.id.btn_lv2))
                .check(matches(isEnabled()));

        // 無効である
        onView(withId(R.id.btn_lv1))
                .check(matches(not(isEnabled())));

        onView(withId(R.id.btn_lv3)).check(
                matches(not(isEnabled())));

        onView(withId(R.id.btn_lv4)).check(
                matches(not(isEnabled())));

        onView(withId(R.id.btn_lv5)).check(
                matches(not(isEnabled())));

        onView(withId(R.id.btn_lv6)).check(
                matches(not(isEnabled())));

        // 右アイコンがfa-playでない
        onView(
                allOf(
                        withId(R.id.lblRight)
                        , hasSibling(withText(R.string.lbl_lv2))
                )
        )
                .check(matches(not(withText(R.string.fa_play))));

        // 右アイコンがfa-playである
        onView(
                allOf(
                        withId(R.id.lblRight)
                        , hasSibling(withText(R.string.lbl_lv1))
                )
        )
                .check(matches(withText(R.string.fa_play)));

        onView(
                allOf(
                        withId(R.id.lblRight)
                        , hasSibling(withText(R.string.lbl_lv3))
                )
        )
                .check(matches(withText(R.string.fa_play)));

        onView(
                allOf(
                        withId(R.id.lblRight)
                        , hasSibling(withText(R.string.lbl_lv4))
                )
        )
                .check(matches(withText(R.string.fa_play)));

        onView(
                allOf(
                        withId(R.id.lblRight)
                        , hasSibling(withText(R.string.lbl_lv5))
                )
        )
                .check(matches(withText(R.string.fa_play)));

        onView(
                allOf(
                        withId(R.id.lblRight)
                        , hasSibling(withText(R.string.lbl_lv6))
                )
        )
                .check(matches(withText(R.string.fa_play)));


    }

    public void testLv6ボタンClickで他のLvボタンが無効になる() {

        // 表示されていないとclickできないのでスクロールさせる
//        TouchUtils.scrollToBottom(this, mActivity, (ViewGroup) mActivity.findViewById(R.id.scrollView));

        onView(withId(R.id.btn_lv6))
                .perform(scrollTo(), click());

        // 有効である
        onView(withId(R.id.btn_lv6))
                .check(matches(isEnabled()));

        // 無効である
        onView(withId(R.id.btn_lv1))
                .check(matches(not(isEnabled())));

        onView(withId(R.id.btn_lv2)).check(
                matches(not(isEnabled())));

        onView(withId(R.id.btn_lv3)).check(
                matches(not(isEnabled())));

        onView(withId(R.id.btn_lv4)).check(
                matches(not(isEnabled())));

        onView(withId(R.id.btn_lv5)).check(
                matches(not(isEnabled())));

        // 右アイコンがfa-playでない
        onView(
                allOf(
                        withId(R.id.lblRight)
                        , hasSibling(withText(R.string.lbl_lv6))
                )
        )
                .check(matches(not(withText(R.string.fa_play))));

        // 右アイコンがfa-playである
        onView(
                allOf(
                        withId(R.id.lblRight)
                        , hasSibling(withText(R.string.lbl_lv1))
                )
        )
                .check(matches(withText(R.string.fa_play)));

        onView(
                allOf(
                        withId(R.id.lblRight)
                        , hasSibling(withText(R.string.lbl_lv2))
                )
        )
                .check(matches(withText(R.string.fa_play)));

        onView(
                allOf(
                        withId(R.id.lblRight)
                        , hasSibling(withText(R.string.lbl_lv3))
                )
        )
                .check(matches(withText(R.string.fa_play)));

        onView(
                allOf(
                        withId(R.id.lblRight)
                        , hasSibling(withText(R.string.lbl_lv4))
                )
        )
                .check(matches(withText(R.string.fa_play)));

        onView(
                allOf(
                        withId(R.id.lblRight)
                        , hasSibling(withText(R.string.lbl_lv5))
                )
        )
                .check(matches(withText(R.string.fa_play)));


    }

    public void testかんたんチェック画面に広告枠が表示されている() {

        onView(allOf(withId(R.id.ad_container), hasSibling(withId(R.id.scroll_main))))
                .check(matches(isCompletelyDisplayed()));
    }

    public void testスワイプでViewPager切り替え() {

        onView(withId(R.id.pager)).perform(swipeLeft());

        onView(withId(R.id.layout_diag))
                .check(matches(isDisplayed()));

        onView(withId(R.id.layout_diag_result))
                .check(matches(not(isDisplayed())));

        onView(allOf(withId(R.id.ad_container), hasSibling(withId(R.id.scroll_diag))))
                .check(matches(isDisplayed()));

        onView(withId(R.id.pager)).perform(swipeRight());

        onView(withId(R.id.scroll_main)).check(matches(isDisplayed()));
    }

    public void testチャレンジモードでstartボタン押下から中止ボタン押下までの画面表示() {

        onView(withId(R.id.pager)).perform(swipeLeft());

        // 初期表示
        onView(withId(R.id.btn_start_diag))
                .check(matches(isDisplayed()));

        onView(withId(R.id.btn_stop))
                .check(matches(not(isDisplayed())));

        onView(withId(R.id.lbl_measuring))
                .check(matches(not(isDisplayed())));

        // スタート
        onView(withId(R.id.btn_start_diag)).perform(click());

        onView(withId(R.id.btn_stop))
                .check(matches(isDisplayed()))
                .check(matches(isEnabled()));

        onView(withId(R.id.lbl_measuring))
                .check(matches(isDisplayed()));

        onView(withId(R.id.btn_start_diag))
                .check(matches(not(isDisplayed())));

        // 中止
        onView(withId(R.id.btn_stop)).perform(click());

        onView(withId(R.id.btn_start_diag))
                .check(matches(isDisplayed()));

        onView(withId(R.id.btn_stop))
                .check(matches(not(isDisplayed())));

        onView(withId(R.id.lbl_measuring))
                .check(matches(not(isDisplayed())));

    }

/*
    public void testチャレンジモードでstartボタン押下から診断終了までの画面表示() {

        onView(withId(R.id.pager))
                .perform(swipeLeft());

        // 診断モードの完了を検知するため
        ViewPager pager = ((MainActivity) mActivity).mViewPager;
        PlaceholderFragment fragment = (PlaceholderFragment) pager.getAdapter().instantiateItem(pager, pager.getCurrentItem());
        CountingIdlingResource idlingResource = new CountingIdlingResource("Diag Running");
        fragment.setmDiagStateListener(new DiagStateListerImpl(idlingResource));
        IdlingPolicies.setMasterPolicyTimeout(3, TimeUnit.MINUTES);
        IdlingPolicies.setIdlingResourceTimeout(3, TimeUnit.MINUTES);
        registerIdlingResources(idlingResource);

        // 診断スタート
        onView(withId(R.id.btn_start_diag))
                .perform(click());


        onView(withId(R.id.lbl_diag_age))
                .check(matches(isDisplayed()));

        // 戻るボタン
        onView(withId(R.id.btn_back_to_diag_top))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(click());

        // 初期表示状態にもどるはず
        onView(withId(R.id.btn_start_diag))
                .check(matches(isDisplayed()));

        onView(withId(R.id.btn_stop))
                .check(matches(not(isDisplayed())));

        onView(withId(R.id.lbl_measuring))
                .check(matches(not(isDisplayed())));


    }
*/

    public void testかんたんチェックで再生後スワイプで戻ってくると再生停止している() {

        onView(withId(R.id.btn_lv1))
                .perform(click());

        onView(withId(R.id.pager))
                .perform(swipeLeft());

        onView(withId(R.id.pager))
                .perform(swipeRight());

        onView(withId(R.id.btn_lv1))
                .check(matches(isEnabled()));

        onView(withId(R.id.btn_lv2))
                .check(matches(isEnabled()));

        onView(withId(R.id.btn_lv3))
                .check(matches(isEnabled()));

        onView(withId(R.id.btn_lv4))
                .check(matches(isEnabled()));

        onView(withId(R.id.btn_lv5))
                .check(matches(isEnabled()));

        onView(withId(R.id.btn_lv6))
                .check(matches(isEnabled()));

        // 右アイコンがfa-playである
        onView(
                allOf(
                        withId(R.id.lblRight)
                        , hasSibling(withText(R.string.lbl_lv1))
                )
        )
                .check(matches(withText(R.string.fa_play)));

        onView(
                allOf(
                        withId(R.id.lblRight)
                        , hasSibling(withText(R.string.lbl_lv2))
                )
        )
                .check(matches(withText(R.string.fa_play)));

        onView(
                allOf(
                        withId(R.id.lblRight)
                        , hasSibling(withText(R.string.lbl_lv3))
                )
        )
                .check(matches(withText(R.string.fa_play)));

        onView(
                allOf(
                        withId(R.id.lblRight)
                        , hasSibling(withText(R.string.lbl_lv4))
                )
        )
                .check(matches(withText(R.string.fa_play)));

        onView(
                allOf(
                        withId(R.id.lblRight)
                        , hasSibling(withText(R.string.lbl_lv5))
                )
        )
                .check(matches(withText(R.string.fa_play)));

        onView(
                allOf(
                        withId(R.id.lblRight)
                        , hasSibling(withText(R.string.lbl_lv6))
                )
        )
                .check(matches(withText(R.string.fa_play)));

    }

    public void testチャレンジモードスタート後スワイプで戻ってくると再生停止している() {

        onView(withId(R.id.pager))
                .perform(swipeLeft());

        onView(withId(R.id.btn_start_diag))
                .perform(click());

        onView(withId(R.id.pager))
                .perform(swipeRight());

        onView(withId(R.id.pager))
                .perform(swipeLeft());

        // 初期表示
        onView(withId(R.id.btn_start_diag))
                .check(matches(isDisplayed()));

        onView(withId(R.id.btn_stop))
                .check(matches(not(isDisplayed())));

        onView(withId(R.id.lbl_measuring))
                .check(matches(not(isDisplayed())));

    }

    private class DiagStateListerImpl implements PlaceholderFragment.DiagStateListener {

        private final CountingIdlingResource mIdlingResource;

        private DiagStateListerImpl(CountingIdlingResource idlingResource) {
            this.mIdlingResource = idlingResource;
        }

        @Override
        public void onDiagStateChanged(boolean running) {
            if (running) {
                mIdlingResource.increment();
            } else {
                mIdlingResource.decrement();
            }
        }
    }
}
