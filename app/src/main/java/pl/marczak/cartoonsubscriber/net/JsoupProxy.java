package pl.marczak.cartoonsubscriber.net;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
                    Log.d("JsoupProxy", "calling url = " + url);
                    document = Jsoup.connect(url).get();
                } catch (Exception ignored) {
                    Log.e("JsoupProxy", "getDocument: " + ignored.getMessage());
                    subscriber.onError(ignored);
                }
                subscriber.onNext(document);
                subscriber.onCompleted();
            }
        });
    }

    public static void printElement(String TAG, Element e) {
        Log.d(TAG, "element: html: " + e.html() + ", text: " + e.text() + ", val: " + e.val() + ", data: "
                + e.data() + ", id: " + e.id() + ", nodeName: " + e.nodeName() + ", tag: " + e.tag()
                + ", tagName: " + e.tagName() + ", \n" + e.outerHtml());
    }

    public static void printElements(String TAG, Elements elements) {
        for (Element el : elements) printElement(TAG, el);
    }
}
