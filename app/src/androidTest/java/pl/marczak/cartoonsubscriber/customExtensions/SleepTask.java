package pl.marczak.cartoonsubscriber.customExtensions;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;

/**
 * @author Lukasz Marczak
 * @since 24.06.16.
 */
public class SleepTask implements ViewAction {
    final long millis;

    public SleepTask(final long millis) {
        this.millis = millis;
    }

    @Override
    public Matcher<View> getConstraints() {
        return isAssignableFrom(View.class);
    }

    @Override
    public String getDescription() {
        return "is drawer open";
    }

    @Override
    public void perform(UiController uiController, View view) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException x) {
        }
    }
}