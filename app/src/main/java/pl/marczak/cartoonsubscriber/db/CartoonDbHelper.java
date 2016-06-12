package pl.marczak.cartoonsubscriber.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Lukasz Marczak
 * @since 03.06.16.
 */
public class CartoonDbHelper extends SQLiteOpenHelper {
    public static final String TAG = CartoonDbHelper.class.getSimpleName();
    private Context ctx;
    //version of database
    private static final int version = 1;
    //database name
    public static final String DB_NAME = "CARTOONS_DB";
    //name of table
    public static final String TABLE_NAME = "Cartoon";
    //column names
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_INFO = "info";
    public static final String KEY_URL = "url";
    public static final String KEY_SUBSCRIBED = "subscribed";
    public static final String KEY_LAST_EPISODE = "last_episode";
    public static String[] FIELDS = new String[]{
            KEY_ID, KEY_NAME, KEY_INFO, KEY_LAST_EPISODE, KEY_LAST_EPISODE, KEY_URL, KEY_SUBSCRIBED
    };

    //sql query to creating table in database
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS "
                    + TABLE_NAME
                    + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + KEY_NAME + " TEXT, "
                    + KEY_INFO + " TEXT, "
                    + KEY_URL + " TEXT, "
                    + KEY_SUBSCRIBED + " SHORT, "
                    + KEY_LAST_EPISODE + " TEXT);";

    //contructor of DBHelper
    public CartoonDbHelper(Context context) {
        super(context, DB_NAME, null, version);
        this.ctx = context;
    }

    public CartoonDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, Context ctx) {
        super(context, DB_NAME, factory, 1);
        this.ctx = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + TABLE_NAME);
        onCreate(db);

    }


    public synchronized void fastAddCartoon(SQLiteDatabase db, String title, String info, String url, String last_episode) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, title);
        contentValues.put(KEY_INFO, info);
        contentValues.put(KEY_URL, url);
        contentValues.put(KEY_SUBSCRIBED, "0");
        contentValues.put(KEY_LAST_EPISODE, last_episode);
        db.insert(TABLE_NAME, null, contentValues);
    }

    public void addCartoon(String title, String info, String url, String last_episode) {
        SQLiteDatabase db = getWritableDatabase();
        fastAddCartoon(db, title, info, url, last_episode);
        db.close();
    }


    public List<Cartoon> getAllCartoons(SQLiteDatabase db) {
        return getCartoons(db, false);
    }

    public List<Cartoon> getSubscribedCartoons(SQLiteDatabase db) {
        return getCartoons(db, true);
    }

    public synchronized List<Cartoon> getCartoons(@Nullable SQLiteDatabase db, String query) {
        Log.d(TAG, "getCartoons: ");
        if (db == null) {
            Log.i(TAG, "getCartoons: nullable db");
            return new ArrayList<>();
        }
        String[] args = new String[]{};
//        Cursor c = db.query(TABLE_NAME, new String[]{KEY_ID, KEY_NAME,
//                        KEY_INFO, KEY_LAST_EPISODE, KEY_SUBSCRIBED, KEY_URL},
//                KEY_NAME + " LIKE '" + query + "' ",
//                null, null, null, KEY_NAME + " ASC");
        Cursor c = db.rawQuery("select * from " + TABLE_NAME + " where " + KEY_NAME + " like '" + query + "';", args);
        c.moveToFirst();
        List<Cartoon> cartoons = new ArrayList<>();
        while (c.moveToNext()) {
            Log.d(TAG, "getCartoons: ");
            cartoons.add(fromCursorCartoon(c));
        }
        c.close();
        return cartoons;
    }

    private List<Cartoon> getCartoons(SQLiteDatabase db, boolean shouldBeSubscribed) {

        Cursor c = db.query(TABLE_NAME, new String[]{KEY_ID, KEY_SUBSCRIBED}, KEY_SUBSCRIBED + " = ?",
                new String[]{"1"}, null, null, "id ASC");
        c.moveToFirst();
        List<Cartoon> cartoons = new ArrayList<>();
        while (c.moveToNext()) {
            if (!shouldBeSubscribed || isSubscribed(c)) {
                cartoons.add(fromCursorCartoon(c));
            }
        }
        c.close();
        return cartoons;
    }

    private boolean isSubscribed(Cursor c) {
        return c.getShort(c.getColumnIndex(KEY_SUBSCRIBED)) == 1;
    }

    public Cartoon fromCursorCartoon(Cursor c) {
        String title = c.getString(c.getColumnIndex(KEY_NAME));
        Log.d(TAG, "fromCursorCartoon: " + title);
        String info = c.getString(c.getColumnIndex(KEY_INFO));
        String url = c.getString(c.getColumnIndex(KEY_URL));
        String last_ep = c.getString(c.getColumnIndex(KEY_LAST_EPISODE));
        return new Cartoon(title, info, url, last_ep);
    }


    public void updateCartoon(String title, String info, boolean isSubscribed, String last_episode) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(KEY_INFO, info);
        cv.put(KEY_LAST_EPISODE, last_episode);
        cv.put(KEY_SUBSCRIBED, isSubscribed);
        db.update(TABLE_NAME, cv, KEY_NAME + " LIKE '" + title + "'", null);
        db.close();
    }

}