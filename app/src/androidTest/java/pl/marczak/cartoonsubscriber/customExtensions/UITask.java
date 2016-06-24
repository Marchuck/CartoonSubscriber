package pl.marczak.cartoonsubscriber.customExtensions;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isRoot;

/**
 * @author Lukasz Marczak
 * @since 24.06.16.
 */
public class UITask implements ViewAction {
    final Runnable runnable;

    public UITask(final Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public Matcher<View> getConstraints() {
        return isRoot();
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void perform(UiController uiController, View view) {
        runnable.run();
    }
}