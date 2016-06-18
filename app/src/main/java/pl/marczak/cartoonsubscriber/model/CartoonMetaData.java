package pl.marczak.cartoonsubscriber.model;

import android.util.Log;

/**
 * @author Lukasz Marczak
 * @since 18.06.16.
 */
public class CartoonMetaData {
    public static final String TAG = CartoonMetaData.class.getSimpleName();
    public String about;
    public String imageUrl;
    public String title;

    public CartoonMetaData() {
    }

    public CartoonMetaData(String about, String imageUrl, String title) {
        this.about = about;
        this.imageUrl = imageUrl;
        this.title = title;
        Log.d(TAG, "CartoonMetaData: " + this.toString());
    }

    @Override
    public String toString() {
        return "about: " + about + ", imageUrl: " + imageUrl + ", title: " + title;
    }
}
