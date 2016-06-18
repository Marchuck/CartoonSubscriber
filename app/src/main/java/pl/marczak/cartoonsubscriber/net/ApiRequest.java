package pl.marczak.cartoonsubscriber.net;

import android.support.annotation.Nullable;
import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.marczak.cartoonsubscriber.db.Episode;
import pl.marczak.cartoonsubscriber.model.CartoonEntity;
import pl.marczak.cartoonsubscriber.utils.Is;
import rx.Observable;
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

    public rx.Observable<List<String>> execute(@Nullable String url) {
        Log.d(TAG, "execute: " + url);
        if (url == null) return execute();
        else return executeQuery(url);
    }

    public rx.Observable<List<String>> executeQuery(List<String> urls) {
        return Observable.from(urls)
                .flatMap(s -> JsoupProxy.getJsoupDocument(s)).map(document -> {
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

    private rx.Observable<List<String>> executeQuery(String url1) {
        Log.i(TAG, "executeQuery: " + url1);
        final List<String> emptyList = new ArrayList<>();
        emptyList.add("");
        emptyList.add("");
        return JsoupProxy.getJsoupDocument(url1).map(document -> {
            Log.d(TAG, "calling document: " + (document == null));
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

    public rx.Observable<CartoonEntity> getEpisodesWithData(String url) {
        Log.i(TAG, "getEpisodesWithData: " + url);
        return JsoupProxy.getJsoupDocument(url).map(document -> {
            Log.d(TAG, "calling document: " + (document == null));
            if (document == null) {
                throw new NullPointerException("Nullable document");
            }
            CartoonEntity entity = new CartoonEntity();

            Log.i(TAG, "received document " + document.title());
            List<Episode> episodes = new ArrayList<>();

            Elements elements = document.getElementsByClass("sonra");
            if (Is.nullable(elements)) Log.w(TAG, "episodes are empty: ");
                // throw new NullPointerException("Nullable episodes");
            else {
                for (Element e : elements) {
                    String url1 = e.attr("href");
                    String title = e.attr("title");
                    Log.d(TAG, "call: " + url1 + "," + title);
                    episodes.add(new Episode(title, url1));
                }
            }

            Log.d(TAG, "call: 1");
            entity.episodes = episodes;
            Elements titles = document.getElementsByClass("iltext");
            Element image = document.getElementById("cat-img-desc");
            Log.d(TAG, "call: 2");
            if (titles.isEmpty())
                entity.about = "";
            else
                entity.about = titles.get(0).text();
            Log.d(TAG, "call: 3");
            entity.imageUrl = imgUrlExtract(image);
            return entity;
        });
    }

    private String imgUrlExtract(Element image) {
        String[] arr = image.outerHtml().split("\"");
        if (arr.length < 2) return null;
        for (String a : arr) if (a.contains("http")) return a;
        return "";
    }
}
