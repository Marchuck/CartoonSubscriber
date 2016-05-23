package pl.marczak.cartoonsubscriber;

import android.support.annotation.Nullable;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * @author Lukasz Marczak
 * @since 22.05.16.
 */
public class ApiRequest {
    public static final String TAG = ApiRequest.class.getSimpleName();

    public static ApiRequest create() {
        return new ApiRequest();
    }

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

    public rx.Observable<List<String>> execute() {
        return execute("http://www.watchcartoononline.com/regular-show-pilot");
    }

    public static void printElement(String TAG, Element e) {
        Log.d(TAG, "element: html: " + e.html() + ", text: " + e.text() + ", val: " + e.val() + ", data: "
                + e.data() + ", id: " + e.id() + ", nodeName: " + e.nodeName() + ", tag: " + e.tag()
                + ", tagName: " + e.tagName() + ", \n" + e.outerHtml());
    }

    public static void printElements(String TAG, Elements elements) {
        for (Element el : elements) printElement(TAG, el);
    }

    public rx.Observable<List<String>> execute(String url) {

        return getJsoupDocument(url).map(new Func1<Document, List<String>>() {
                    @Override
                    public List<String> call(@Nullable Document document) {
                        if (document == null)
                            return new ArrayList<String>() {
                                {
                                    add("");
                                    add("");
                                }
                            };
                        Log.i(TAG, "received document " + document.title());
                        Elements newest = document.getElementsByClass("menustyle");
                        if (newest == null || newest.size() == 0) return new ArrayList<String>() {
                            {
                                add("");
                                add("");
                            }
                        };
                        Element element = newest.get(1);
                        String[] regularElems = element.text().split("Regular Show");
                        List<String> lst = new ArrayList<>();
                        for (int j = 1; j < regularElems.length; j++)
                            lst.add(regularElems[j].trim());
                        for (String s : lst) Log.i(TAG, "call: [" + s + "]");
                        return Arrays.asList(regularElems);
                    }
                });
    }
}
