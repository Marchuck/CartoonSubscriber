package pl.marczak.cartoonsubscriber.net;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import pl.marczak.cartoonsubscriber.db.Cartoon;
import pl.marczak.cartoonsubscriber.db.Episode;
import pl.marczak.cartoonsubscriber.db.RealmCartoon;
import pl.marczak.cartoonsubscriber.model.CartoonEntity;
import pl.marczak.cartoonsubscriber.utils.Is;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

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

    public static List<Cartoon> nonRealmCartoons(RealmResults<RealmCartoon> cartoons) {
        List<Cartoon> out = new ArrayList<>();
        for (RealmCartoon c : cartoons) {
            out.add(new Cartoon(c.getTitle(), c.getInfo(), c.getUrl(), c.getLastEpisode()));
        }
        return out;
    }

    public rx.Observable<List<Episode>> checkNewestEpisodes(Context context) {
        Realm realm = Realm.getInstance(context);
        RealmResults<RealmCartoon> cartoons =
                realm.where(RealmCartoon.class).equalTo("isSubscribed", true).findAll();
        if (Is.nonEmpty(cartoons)) {
            List<Cartoon> nonEmptyCartoons = nonRealmCartoons(cartoons);
            return Observable.from(nonEmptyCartoons)
                    .flatMap(cartoon -> getLastEpisode(cartoon.url))
                    .flatMap(episode -> {
                        for (Cartoon c : nonEmptyCartoons) {
                            if (isNewEpisode(c, episode)) {
                                return Observable.just(episode);
                            }
                        }
                        return Observable.empty();
                    }).toList();
        }
        return Observable.empty();
    }

    private boolean isNewEpisode(Cartoon c, Episode episode) {
        return cartoonsTheSame(c, episode) && newEpisode(c, episode);
    }

    private boolean newEpisode(Cartoon c, Episode episode) {
        return !c.last_episode.equalsIgnoreCase(episode.title);
    }

    private boolean cartoonsTheSame(Cartoon c, Episode episode) {
        return episode.title.contains(c.title);
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

    public rx.Observable<Episode> getLastEpisode(String url) {
        Log.i(TAG, "getEpisodesWithData: " + url);
        return JsoupProxy.getJsoupDocument(url).map(document -> {
                    Log.d(TAG, "calling document: " + (document == null));
                    if (document == null) {
                        throw new NullPointerException("Nullable document");
                    }
                    Episode episode = null;
                    Log.i(TAG, "received document " + document.title());

                    Elements elements = document.getElementsByClass("sonra");
                    if (Is.nullable(elements)) Log.w(TAG, "episodes are empty: ");
                        // throw new NullPointerException("Nullable episodes");
                    else {
                        Element e = elements.first();
                        String url1 = e.attr("href");
                        String title = e.attr("title");
                        Log.d(TAG, "call: " + url1 + "," + title);
                        episode = new Episode(title, url);
                    }
                    return episode;
                }

        );
    }

    private String imgUrlExtract(Element image) {
        String[] arr = image.outerHtml().split("\"");
        if (arr.length < 2) return null;
        for (String a : arr) if (a.contains("http")) return a;
        return "";
    }
}
