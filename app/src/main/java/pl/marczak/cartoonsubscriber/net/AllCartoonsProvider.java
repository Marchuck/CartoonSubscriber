package pl.marczak.cartoonsubscriber.net;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import pl.marczak.cartoonsubscriber.db.Cartoon;
import pl.marczak.cartoonsubscriber.db.DBSaver;
import pl.marczak.cartoonsubscriber.db.PersistanceManager;
import pl.marczak.cartoonsubscriber.db.RealmCartoon;
import pl.marczak.cartoonsubscriber.utils.Is;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author Lukasz Marczak
 * @since 26.05.16.
 */
public class AllCartoonsProvider {
    public static final String TAG = AllCartoonsProvider.class.getSimpleName();
    private static String url = "http://www.watchcartoononline.com/cartoon-list";

    public static rx.Observable<List<Cartoon>> getCartoons(Context context) {
        Realm realm = Realm.getInstance(context);
        RealmResults<RealmCartoon> cartoons = realm.where(RealmCartoon.class).findAll();
        boolean isEmpty = Is.nullable(cartoons);
        realm.close();
        if (isEmpty) return getCartoons().map(cartoons1 -> {
            saveCartoons(cartoons1, context);
            return cartoons1;
        });
        else return fromRealmCartoons(context);
    }

    private static Observable<List<Cartoon>> fromRealmCartoons(Context context) {
        Log.d(TAG, "fromRealmCartoons: ");
        Realm realm = Realm.getInstance(context);
        RealmResults<RealmCartoon> realmCartoons = realm.where(RealmCartoon.class).findAllSorted("title");
        List<Cartoon> cartoons = asPojoCartoons(realmCartoons);
        realm.close();
        return Observable.just(cartoons);
    }

    private static List<Cartoon> asPojoCartoons(RealmResults<RealmCartoon> realmCartoons) {
        Log.d(TAG, "asPojoCartoons: ");
        List<Cartoon> cartoons = new ArrayList<>();
        for (RealmCartoon r : realmCartoons) {
            cartoons.add(new Cartoon(r.getTitle(), r.getInfo(), r.getUrl(), r.getLastEpisode()));
        }
        return cartoons;
    }

    private static void saveCartoons(List<Cartoon> cartoons, Context context) {
        Log.d(TAG, "saveCartoons: ");
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();
        for (Cartoon c : cartoons) {
            RealmCartoon cartoon = createRealmCartoon(c);
            realm.copyToRealmOrUpdate(cartoon);
        }
        realm.commitTransaction();
        realm.close();
    }

    private static RealmCartoon createRealmCartoon(Cartoon c) {
        return new RealmCartoon(PersistanceManager.uuid(),
                c.last_episode,
                c.title,
                false,
                c.info, c.url, "null", 0);
    }

    public static rx.Observable<List<Cartoon>> getCartoons() {
        return JsoupProxy.getJsoupDocument(url).map(new Func1<Document, List<Cartoon>>() {
            private boolean canReadFurther;

            @Override
            public List<Cartoon> call(@Nullable Document document) {
                List<Cartoon> cartoons = new ArrayList<>();
                Log.d(TAG, "printing li's");
                if (document != null) {
                    Elements elements = document.select("li");
                    Log.e(TAG, "elements size: " + elements.size());
                    // JsoupProxy.printElements(TAG, elements);
                    for (Element el : elements) {
                        String cartoonName = el.text();
                        if (cartoonName.equalsIgnoreCase("#")) {
                            canReadFurther = true;
                        }
                        if (!canReadFurther || cartoonName.length() < 2) continue;

                        String[] aa = el.html().split("\"");
                        if (aa.length > 1) {
                            cartoons.add(new Cartoon(cartoonName, "", aa[1], ""));
                        }
                    }
                }
                return cartoons;
            }
        });
    }

    public static rx.Observable<List<Cartoon>> fetchCartoons(@Nullable final DBSaver<Cartoon> saver) {
        return JsoupProxy.getJsoupDocument(url).map(new Func1<Document, List<Cartoon>>() {
            private boolean canReadFurher;

            @Override
            public List<Cartoon> call(Document document) {
                List<Cartoon> cartoons = new ArrayList<>();
                Log.e(TAG, "printing li's");
                Elements elements = document.select("li");
                // JsoupProxy.printElements(TAG, elements);
                for (Element el : elements) {
                    String cartoonName = el.text();
                    if (cartoonName.equalsIgnoreCase("#")) {
                        canReadFurher = true;
                    }
                    if (!canReadFurher || cartoonName.length() < 2) continue;

                    String[] aa = el.html().split("\"");
                    if (aa.length > 1) {
                        cartoons.add(new Cartoon(cartoonName, "", aa[1], ""));

                    }
                }
                if (saver != null) saver.onSave(cartoons);
                return cartoons;
            }
        });
    }


}
