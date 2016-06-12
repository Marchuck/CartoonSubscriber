package pl.marczak.cartoonsubscriber.utils;

import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 08.06.16.
 */
public interface TextChangeUpdater<T> {
    void onUpdate(List<T> update);
}
