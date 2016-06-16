package pl.marczak.cartoonsubscriber.utils;

import android.content.Context;
import android.support.v7.widget.SearchView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pl.marczak.cartoonsubscriber.db.Cartoon;
import pl.marczak.cartoonsubscriber.db.CartoonDbHelper;
import pl.marczak.cartoonsubscriber.di.App;
import rx.Observable;

/**
 * @author Lukasz Marczak
 * @since 08.06.16.
 */
public class CartoonSuggestionsEngine {
    public static final String TAG = CartoonSuggestionsEngine.class.getSimpleName();
    CartoonDbHelper dbHelper;

    public CartoonSuggestionsEngine(Context context) {
        initDBHelper(context);
    }

    private void initDBHelper(Context context) {
        Log.d(TAG, "initDBHelper: ");
        dbHelper = new CartoonDbHelper(context);
    }

    public rx.Observable<String> emitInputs(final SearchView searchView) {
        Log.d(TAG, "emitInputs: ");
        return Observable.create((Observable.OnSubscribe<String>) subscriber -> {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.d(TAG, "onQueryTextSubmit: " + query);
                    subscriber.onNext(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        });
    }

    public rx.Observable<List<Cartoon>> getCartoons(final Context c, final SearchView searchView) {

        return emitInputs(searchView).map(s -> {
            Log.i(TAG, "query: " + s);
            List<Cartoon> cartoons = dbHelper.getCartoons(App.getInstance(c.getApplicationContext()).readableDatabase, s);
            return cartoons;
        });
    }


    public rx.Observable<List<Cartoon>> getCartoons(final SearchView searchView, final List<Cartoon> cartoons) {
        return emitInputs(searchView).map(s -> {
            Log.i("", "query: " + s);

            List<Cartoon> cartoons_ = new ArrayList<>();
            for (Cartoon c : cartoons) {
                if (c.title.startsWith(s)) {
                    cartoons_.add(c);
                }
            }
            return cartoons_;
        });
    }

}
