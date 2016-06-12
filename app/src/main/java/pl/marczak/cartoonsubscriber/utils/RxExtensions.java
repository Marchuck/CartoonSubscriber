package pl.marczak.cartoonsubscriber.utils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Lukasz Marczak
 * @since 26.05.16.
 */
public class RxExtensions {

    public static <T> Observable<T> allOnUi(Observable<T> observable) {
        return observable.subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> Observable<T> resultsOnUi(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
