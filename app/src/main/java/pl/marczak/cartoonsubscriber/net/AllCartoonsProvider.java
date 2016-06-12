package pl.marczak.cartoonsubscriber.net;

import android.support.annotation.Nullable;
import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import pl.marczak.cartoonsubscriber.db.Cartoon;
import pl.marczak.cartoonsubscriber.db.DBSaver;
import rx.functions.Func1;

/**
 * @author Lukasz Marczak
 * @since 26.05.16.
 */
public class AllCartoonsProvider {
    public static final String TAG = AllCartoonsProvider.class.getSimpleName();
    private static String url = "http://www.watchcartoononline.com/cartoon-list";

    public static rx.Observable<List<Cartoon>> fetchCartoons(@Nullable final DBSaver<Cartoon> saver) {
        return JsoupProxy.getJsoupDocument(url).map(new Func1<Document, List<Cartoon>>() {
            private boolean canReadFurher;

            @Override
            public List<Cartoon> call(Document document) {
                List<Cartoon> cartoons = new ArrayList<>();
                Log.e(TAG, "printing li's");
                Elements elements = document.select("li");
                JsoupProxy.printElements(TAG, elements);
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
