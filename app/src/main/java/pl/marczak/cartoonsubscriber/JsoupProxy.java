package pl.marczak.cartoonsubscriber;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;

/**
 * @author Lukasz Marczak
 * @since 26.05.16.
 */
public class JsoupProxy {
    public static rx.Observable<Document> getJsoupDocument(final String url) {
        return Observable.create(new Observable.OnSubscribe<Document>() {

            @Override
            public void call(Subscriber<? super Document> subscriber) {
                Document document = null;
                try {
                    document = Jsoup.connect(url).get();
                } catch (IOException ignored) {
                    Log.e("JsoupProxy", "getDocument: " + ignored.getMessage());
                }
                subscriber.onNext(document);
                subscriber.onCompleted();
            }
        });
    }
}
