package pl.marczak.cartoonsubscriber.customExtensions;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;

/**
 * @author Lukasz Marczak
 * @since 24.06.16.
 */
public class SearchViewExtensions {

    public static ViewAction typeText(String text) {
        return typeText(text, true);
    }

    public static ViewAction typeText(String text, boolean submit) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(android.support.v7.widget.SearchView.class);

            }

            @Override
            public String getDescription() {
                return "entering text " + text;
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((android.support.v7.widget.SearchView) view).setQuery(text, submit);
            }
        };
    }

    public static Matcher<View> withText(final String str) {
        return withText(Matchers.is(str));
    }


    public static Matcher<View> withText(final Matcher<String> stringMatcher) {
        //checkNotNull(stringMatcher);
        return new BoundedMatcher<View, android.support.v7.widget.SearchView>(android.support.v7.widget.SearchView.class) {
            @Override
            protected boolean matchesSafely(android.support.v7.widget.SearchView item) {
                return stringMatcher.matches(item.getQuery().toString());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with text: ");
                stringMatcher.describeTo(description);
            }
        };
    }
}
