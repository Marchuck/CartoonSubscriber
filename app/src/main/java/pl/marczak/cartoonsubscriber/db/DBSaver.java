package pl.marczak.cartoonsubscriber.db;

import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 08.06.16.
 */
public interface DBSaver<T> {
    void onSave(List<T> collection);
}
