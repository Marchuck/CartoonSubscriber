package pl.marczak.cartoonsubscriber;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.threeten.bp.Instant;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author Lukasz Marczak
 * @since 22.05.16.
 */
public class PeriodicHttpRequest extends BroadcastReceiver {
    public static final String TAG = PeriodicHttpRequest.class.getSimpleName();

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");

        ApiRequest.create().execute().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> strings) {
                        Log.d(TAG, "call: ");
                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(context)
                                        .setSmallIcon(R.drawable.regular)
                                        .setContentTitle(strings.get(0))
                                        .setContentText(strings.get(1));
                        mBuilder.build();


                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(context, "No available content", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static long _12hoursInMillis = TimeUnit.HOURS.toMillis(12);

    public void setAlarm(Context context) {
        Log.d(TAG, "setAlarm: ");
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, PeriodicHttpRequest.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
      //  long timeNow = Instant.now().getNano();

        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000*60 , pi); // Millisec * Second * Minute
    }

    public void cancelAlarm(Context context) {
        Log.d(TAG, "cancelAlarm: ");
        Intent intent = new Intent(context, PeriodicHttpRequest.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
