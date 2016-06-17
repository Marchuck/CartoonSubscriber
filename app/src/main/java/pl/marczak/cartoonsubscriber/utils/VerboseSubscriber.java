package pl.marczak.cartoonsubscriber.utils;

import android.util.Log;

import rx.Subscriber;

/**
 * @author Lukasz Marczak
 * @since 08.06.16.
 */
public abstract class VerboseSubscriber<T> extends Subscriber<T> {
    //public static final String TAG = VerboseSubscriber.class.getSimpleName();
    public String TAG;

    public VerboseSubscriber(String TAG) {
        this.TAG = TAG;
    }

    @Override
    public void onCompleted() {
        Log.d(TAG, "onCompleted: ");
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "onError: " + e.getMessage());
        e.printStackTrace();
    }
}
