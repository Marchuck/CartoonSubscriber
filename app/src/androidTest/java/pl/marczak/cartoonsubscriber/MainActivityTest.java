package pl.marczak.cartoonsubscriber;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.Gravity;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import pl.marczak.cartoonsubscriber.customExtensions.DrawerExtensions;
import pl.marczak.cartoonsubscriber.customExtensions.SearchViewExtensions;
import pl.marczak.cartoonsubscriber.customExtensions.SleepTask;
import pl.marczak.cartoonsubscriber.right_tab.RightNavigatorFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * @author Lukasz Marczak
 * @since 13.06.16.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainActivityTest {
    public static final String TAG = MainActivityTest.class.getSimpleName();
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    private RightNavigatorFragment startRightFragment() {
        FragmentActivity activity = mActivityRule.getActivity();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        RightNavigatorFragment myFragment = new RightNavigatorFragment();
        transaction.add(myFragment, RightNavigatorFragment.TAG);
        transaction.commit();
        return myFragment;
    }

    @Test
    public void atFirstOpenDrawer() {
        startRightFragment();
        onView(withId(R.id.drawer_layout)).perform(DrawerExtensions.openDrawer(Gravity.RIGHT))
        .perform(new SleepTask(1000));
    }

    @Test
    public void butCheckIfOpened() {
        onView(withId(R.id.drawer_layout))
                .perform(DrawerExtensions.openDrawer(Gravity.RIGHT))
                .perform(new SleepTask(1000))
                .check((view, noViewFoundException) -> {

                    if (view instanceof DrawerLayout) {
                        boolean isDrawerOpened = ((DrawerLayout) view).isDrawerOpen(Gravity.RIGHT);
                        if (isDrawerOpened) return;
                        throw new IllegalStateException("drawer not opened");
                    }
                    throw new ClassCastException("cannot cast to drawerLayout");
                });
    }

    @Test
    public void enterText() {
        enter("regular");
    }

    private void enter(String text) {
        onView(withId(R.id.search_view_cartoon_list))
                .perform(SearchViewExtensions.typeText(text, true))
                .check(matches(SearchViewExtensions.withText(text)));
    }

    private static int getResourceId(String s) {
        Context targetContext = InstrumentationRegistry.getTargetContext();
        String packageName = targetContext.getPackageName();
        return targetContext.getResources().getIdentifier(s, "id", packageName);
    }
}
