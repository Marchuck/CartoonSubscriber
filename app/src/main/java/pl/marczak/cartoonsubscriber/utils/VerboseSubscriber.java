package pl.marczak.cartoonsubscriber.utils;

import android.util.Log;

import rx.Subscriber;

/**
 * @author Lukasz Marczak
 * @since 08.06.16.
 */
public abstract class VerboseSubscriber<T> extends Subscriber<T> {
    public static final String TAG = VerboseSubscriber.class.getSimpleName();

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "onError: " + e.getMessage());
        e.printStackTrace();
    }
}
