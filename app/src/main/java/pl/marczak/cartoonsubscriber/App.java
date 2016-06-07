package pl.marczak.cartoonsubscriber;

import android.app.Application;
import android.content.Context;

import com.jakewharton.threetenabp.AndroidThreeTen;

import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 22.05.16.
 */
public class App extends Application {


    public List<String> episodes;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }

    public static App getInstance(Context context) {
        return ((App) context);

    }
}
