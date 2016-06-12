package pl.marczak.cartoonsubscriber.db;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 12.06.16.
 */
public class CartoonsLoader extends android.support.v4.content.AsyncTaskLoader<List<Cartoon>> {

    public CartoonsLoader(Context context) {
        super(context);
    }


    @Override
    public List<Cartoon> loadInBackground() {
        // this is just a simple query, could be anything that gets a cursor
        CartoonsProvider provider = new CartoonsProvider();
        provider.start();
        Cursor c = provider.query(CartoonsProvider.URI_CARTOONS, null, null, null, null);
        List<Cartoon> cartoons = new ArrayList<>();
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String title = c.getString(c.getColumnIndex(CartoonDbHelper.KEY_NAME));
                    String url = c.getString(c.getColumnIndex(CartoonDbHelper.KEY_INFO));
                    String last = c.getString(c.getColumnIndex(CartoonDbHelper.KEY_LAST_EPISODE));
                    String info = c.getString(c.getColumnIndex(CartoonDbHelper.KEY_INFO));
                    cartoons.add(new Cartoon(title, info, url, last));
                } while (c.moveToNext());
            }
            c.close();
        }

        return cartoons;
    }
}
