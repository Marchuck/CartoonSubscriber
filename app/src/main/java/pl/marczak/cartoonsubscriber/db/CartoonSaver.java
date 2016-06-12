package pl.marczak.cartoonsubscriber.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 08.06.16.
 */
public class CartoonSaver implements DBSaver<Cartoon> {
    public static final String TAG = CartoonSaver.class.getSimpleName();
    private Context ctx;

    public CartoonSaver(Context ctx) {
        this.ctx = ctx;

    }

    @Override
    public void onSave(List<Cartoon> collection) {
        CartoonDbHelper helper = new CartoonDbHelper(ctx);
        SQLiteDatabase db = helper.getWritableDatabase();
        for (Cartoon cartoon : collection) {
            Log.d(TAG, "saving next episode: " + cartoon.title + ", " + cartoon.url);
            helper.fastAddCartoon(db, cartoon.title, cartoon.info, cartoon.url, cartoon.last_episode);
        }
        db.close();
    }
}
