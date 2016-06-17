package pl.marczak.cartoonsubscriber.di;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import pl.marczak.cartoonsubscriber.db.Cartoon;
import pl.marczak.cartoonsubscriber.db.CartoonDbHelper;
import pl.marczak.cartoonsubscriber.db.CartoonSaver;
import pl.marczak.cartoonsubscriber.net.AlarmManagerBroadcastReceiver;
import pl.marczak.cartoonsubscriber.net.AllCartoonsProvider;
import pl.marczak.cartoonsubscriber.utils.VerboseSubscriber;
import rx.schedulers.Schedulers;

/**
 * @author Lukasz Marczak
 * @since 22.05.16.
 */
public class App extends android.support.multidex.MultiDexApplication {
    public static final String TAG = App.class.getSimpleName();
    public SQLiteDatabase readableDatabase;
    public List<String> episodes;
    AlarmManagerBroadcastReceiver alarm;
    private AppComponent component;

    public static AppComponent getAppComponent(Context context) {
        App app = (App) context.getApplicationContext();
        if (app.component == null) {
            app.component = DaggerAppComponent.builder()
                    .appModule(app.getApplicationModule())
                    .build();
        }
        return app.component;
    }

    public static App getInstance(Context context) {
        return ((App) context.getApplicationContext());

    }

    protected AppModule getApplicationModule() {
        return new AppModule(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");

        if (isEmptyDb()) {
            Log.e(TAG, "onCreate: database is empty");

            AllCartoonsProvider.fetchCartoons(new CartoonSaver(this)).subscribeOn(Schedulers.computation())
                    .subscribe(new VerboseSubscriber<List<Cartoon>>(TAG) {

                        @Override
                        public void onNext(List<Cartoon> cartoons) {
                            Log.e(TAG, "onNext: " + cartoons.size());
                            readableDatabase =  new CartoonDbHelper(App.this).getReadableDatabase();
                        }
                    });
        } else {
            Log.e(TAG, "onCreate: database not empty");    readableDatabase =  new CartoonDbHelper(App.this).getReadableDatabase();
        }

    }

    private boolean isEmptyDb() {
        CartoonDbHelper helper = new CartoonDbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + CartoonDbHelper.TABLE_NAME, null);
        boolean rowExists = mCursor.moveToFirst();
        mCursor.close();
        db.close();
        return !rowExists;
    }
}
