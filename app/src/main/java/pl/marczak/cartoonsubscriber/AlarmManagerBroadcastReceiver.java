package pl.marczak.cartoonsubscriber;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author Lukasz Marczak
 * @since 23.05.16.
 */

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = AlarmManagerBroadcastReceiver.class.getSimpleName();
    final public static String ONE_TIME = "onetime";

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        ApiRequest.create().execute().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> strings) {
                        Log.d("", "call: ");
                        App.instance.episodes = strings;
                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(context)
                                        .setSmallIcon(R.drawable.regular)
                                        .setAutoCancel(true)
                                        .setSound(getSoundRes())
                                        .setContentTitle(strings.get(0))
                                        .setContentText(strings.get(1));

                        Intent intent = new Intent(context, RecentEpisodeActivity.class);
                        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
                        mBuilder.setContentIntent(pi);
                        NotificationManager mNotificationManager =
                                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(0, mBuilder.build());

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(context, "No available content", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "call: " + throwable.getMessage());
                        throwable.printStackTrace();
                    }
                });

    }

    private Uri getSoundRes() {
        Uri path = Uri.parse("android.resource://pl.marczak.cartoonsubscriber/raw/win95");
        return path;
    }

    public void SetAlarm(Context context) {
        Log.d(TAG, "SetAlarm: ");
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        //After after 30 seconds
        long TWELVE_HOURS= TimeUnit.HOURS.toMillis(12);
//        long TWELVE_HOURS= TimeUnit.MINUTES.toMillis(1);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), TWELVE_HOURS, pi);
    }

    public void CancelAlarm(Context context) {
        Log.d(TAG, "CancelAlarm: ");
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public void setOnetimeTimer(Context context) {
        Log.d(TAG, "setOnetimeTimer: ");
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
    }
}