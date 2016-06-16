package pl.marczak.cartoonsubscriber.net;

import android.util.Log;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;

/**
 * @author Lukasz Marczak
 * @since 22.05.16.
 */
public class ApiRequest {
    public static final String TAG = ApiRequest.class.getSimpleName();


    public static ApiRequest create() {
        return new ApiRequest();
    }

    public static void printElement(String TAG, Element e) {
        Log.d(TAG, "element: html: " + e.html() + ", text: " + e.text() + ", val: " + e.val() + ", data: "
                + e.data() + ", id: " + e.id() + ", nodeName: " + e.nodeName() + ", tag: " + e.tag()
                + ", tagName: " + e.tagName() + ", \n" + e.outerHtml());
    }

    public static void printElements(String TAG, Elements elements) {
        for (Element el : elements) printElement(TAG, el);

    }

    public rx.Observable<List<String>> execute() {
        return executeQuery("http://www.watchcartoononline.com/regular-show-pilot");
    }

    public rx.Observable<List<String>> execute(String url) {
        return executeQuery(url);
    }

    public rx.Observable<List<String>> executeQuery(List<String> urls) {
        return Observable.from(urls)
                .flatMap(JsoupProxy::getJsoupDocument)
                .map(document -> {
                    List<String> emptyList = new ArrayList<>();
                    emptyList.add("");
                    emptyList.add("");
                    if (document == null) {
                        return emptyList;
                    }
                    Log.i(TAG, "received document " + document.title());
                    Elements newest = document.getElementsByClass("menustyle");
                    if (newest == null || newest.size() == 0) return emptyList;
                    Element element = newest.get(1);
                    String[] regularElems = element.text().split("Regular Show");
                    List<String> lst = new ArrayList<>();
                    for (int j = 1; j < regularElems.length; j++)
                        lst.add(regularElems[j].trim());
                    for (String s : lst) Log.i(TAG, "call: [" + s + "]");
                    return Arrays.asList(regularElems);
                });
    }

    private rx.Observable<List<String>> executeQuery(String url) {
        List<String> emptyList = new ArrayList<>();
        emptyList.add("");
        emptyList.add("");
        return JsoupProxy.getJsoupDocument(url).map(document -> {
            if (document == null) {
                return emptyList;
            }
            Log.i(TAG, "received document " + document.title());
            Elements newest = document.getElementsByClass("menustyle");
            if (newest == null || newest.size() == 0) return emptyList;
            Element element = newest.get(1);
            String[] regularElems = element.text().split("Regular Show");
            List<String> lst = new ArrayList<>();
            for (int j = 1; j < regularElems.length; j++)
                lst.add(regularElems[j].trim());
            for (String s : lst) Log.i(TAG, "call: [" + s + "]");
            return Arrays.asList(regularElems);
        });
    }
}
