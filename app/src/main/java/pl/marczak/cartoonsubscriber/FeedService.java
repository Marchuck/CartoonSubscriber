package pl.marczak.cartoonsubscriber;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class FeedService extends Service {
    public static final String TAG = FeedService.class.getSimpleName();
    public FeedService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.i(TAG, "onBind: ");
        return null;
    }

    PeriodicHttpRequest alarm = new PeriodicHttpRequest();

    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        alarm.setAlarm(this);
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d(TAG, "onStart: ");
        alarm.setAlarm(this);
    }
}
