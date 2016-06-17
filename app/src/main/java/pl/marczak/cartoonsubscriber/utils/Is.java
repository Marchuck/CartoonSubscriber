package pl.marczak.cartoonsubscriber.utils;

import java.util.Collection;

/**
 * @author Lukasz Marczak
 * @since 17.06.16.
 */
public class Is {
    public static <T> boolean nullable(Collection<T> collection) {
        return collection == null || collection.size() == 0;
    }

    public static <T> boolean nonEmpty(Collection<T> collection) {
        return collection != null && collection.size() > 0;
    }

    public static String humanReads(boolean b) {
        return b ? "yes" : "no";
    }
}
