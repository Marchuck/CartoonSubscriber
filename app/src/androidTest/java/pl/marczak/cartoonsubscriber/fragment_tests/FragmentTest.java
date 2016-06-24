package pl.marczak.cartoonsubscriber.fragment_tests;

import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import pl.marczak.cartoonsubscriber.R;
import pl.marczak.cartoonsubscriber.right_tab.RightNavigatorFragment;
import pl.marczak.cartoonsubscriber.utils.TestFragmentActivity;

/**
 * @author Lukasz Marczak
 * @since 24.06.16.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class FragmentTest extends ActivityInstrumentationTestCase2<TestFragmentActivity> {
    private TestFragmentActivity mActivity;

    public FragmentTest() {
        super(TestFragmentActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
    }

    private Fragment startFragment(Fragment fragment) {
        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.activity_test_fragment_linearlayout, fragment, "tag");
        transaction.commit();
        getInstrumentation().waitForIdleSync();
        Fragment frag = mActivity.getSupportFragmentManager().findFragmentByTag("tag");
        return frag;
    }

    @Test
    public void testFragment() {
        RightNavigatorFragment fragment = new RightNavigatorFragment();

        Fragment frag = startFragment(fragment);
    }
}