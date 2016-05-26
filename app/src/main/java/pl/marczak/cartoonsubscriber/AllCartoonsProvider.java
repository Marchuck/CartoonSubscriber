package pl.marczak.cartoonsubscriber;

import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import pl.marczak.cartoonsubscriber.cartoononline.Cartoon;
import rx.functions.Func1;

/**
 * @author Lukasz Marczak
 * @since 26.05.16.
 */
public class AllCartoonsProvider {
    public static final String TAG = AllCartoonsProvider.class.getSimpleName();
    private static String url = "http://www.watchcartoononline.com/cartoon-list";

    public static rx.Observable<List<Cartoon>> getCartoons() {
        return JsoupProxy.getJsoupDocument(url).map(new Func1<Document, List<Cartoon>>() {
            @Override
            public List<Cartoon> call(Document document) {
                List<Cartoon> cartoons = new ArrayList<Cartoon>();
                Log.e(TAG, "printing li's");
                Elements elements = document.select("li");
                JsoupProxy.printElements(TAG, elements);
                for (Element el : elements) {
                    String[] aa = el.html().split("\"");
                    if (aa.length > 1) {
                        cartoons.add(new Cartoon(el.text(), aa[1]));
                 //       Log.d(TAG, "title: " + el.text() + '\n' + "url: " + aa[1]);
                    }
                }

                return cartoons;
            }
        });
    }
}
