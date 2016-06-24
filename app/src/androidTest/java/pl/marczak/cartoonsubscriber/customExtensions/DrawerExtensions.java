package pl.marczak.cartoonsubscriber.customExtensions;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;

/**
 * @author Lukasz Marczak
 * @since 24.06.16.
 */
public final class DrawerExtensions {

    public static ViewAction closeDrawer(int gravity) {
        return new ViewAction() {

            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(DrawerLayout.class);
            }

            @Override
            public String getDescription() {
                return "close drawer on " + ((gravity == Gravity.LEFT) ? "left" : "right");
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((DrawerLayout) view).closeDrawer(gravity);
            }
        };
    }

    public static ViewAction openDrawer(int gravity) {
        return new ViewAction() {

            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(DrawerLayout.class);
            }

            @Override
            public String getDescription() {
                return "close drawer on " + ((gravity == Gravity.LEFT) ? "left" : "right");
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((DrawerLayout) view).openDrawer(gravity);
            }
        };
    }

}
