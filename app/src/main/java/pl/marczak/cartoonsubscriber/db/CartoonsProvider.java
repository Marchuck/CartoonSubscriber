package pl.marczak.cartoonsubscriber.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import javax.inject.Inject;

import pl.marczak.cartoonsubscriber.di.App;

/**
 * @author Lukasz Marczak
 * @since 12.06.16.
 */
public class CartoonsProvider extends ContentProvider {
    public static final String TAG = CartoonsProvider.class.getSimpleName();

    @Inject
    CartoonDbHelper cartoonDbHelper;
    // All URIs share these parts
    public static final String AUTHORITY = "pl.marczak.cartoonsubscriber";
    public static final String SCHEME = "content://";

    // URIs
    // Used for all persons
    public static final String CARTOONS = SCHEME + AUTHORITY + "/" + CartoonDbHelper.TABLE_NAME;
    public static final Uri URI_CARTOONS = Uri.parse(CARTOONS);
    // Used for a single person, just add the id to the end
    public static final String PERSON_BASE = CARTOONS + "/";

    public CartoonsProvider() {

    }
    public void start(){
        App.getAppComponent(getContext()).inject(this);
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate: ");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@Nullable Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor result = null;
        Log.d(TAG, "query: ");
        if (URI_CARTOONS.equals(uri)) {
            Log.d(TAG, "query: 1");
            result = cartoonDbHelper
                    .getReadableDatabase()
                    .query(CartoonDbHelper.TABLE_NAME, CartoonDbHelper.FIELDS, null, null, null,
                            null, null, null);
            result.setNotificationUri(getContext().getContentResolver(), URI_CARTOONS);
        } else if (uri.toString().startsWith(PERSON_BASE)) {
            Log.d(TAG, "query: 2");
            final long id = Long.parseLong(uri.getLastPathSegment());
            result = cartoonDbHelper
                    .getReadableDatabase()
                    .query(CartoonDbHelper.TABLE_NAME, CartoonDbHelper.FIELDS,
                            CartoonDbHelper.KEY_ID + " IS ?",
                            new String[]{String.valueOf(id)}, null, null,
                            null, null);
            result.setNotificationUri(getContext().getContentResolver(), URI_CARTOONS);
        } else {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        return result;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
