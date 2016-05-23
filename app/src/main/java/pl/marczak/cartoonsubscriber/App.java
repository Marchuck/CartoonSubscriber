package pl.marczak.cartoonsubscriber;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedTransferQueue;

/**
 * @author Lukasz Marczak
 * @since 22.05.16.
 */
public class App extends Application {


    public List<String> episodes;
    public static App instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AndroidThreeTen.init(this);
    }
}
