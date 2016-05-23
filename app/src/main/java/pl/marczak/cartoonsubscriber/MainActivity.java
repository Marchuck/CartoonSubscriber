package pl.marczak.cartoonsubscriber;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    FloatingActionButton fab;
    AlarmManagerBroadcastReceiver alarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
          long _12hoursInMillis = TimeUnit.MINUTES.toMillis(5);
        Log.d(TAG, "onCreate: "+_12hoursInMillis);

        alarm = new AlarmManagerBroadcastReceiver();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                //Intent i = new Intent(MainActivity.this, FeedService.class);
                //startService(i);
                Context context = MainActivity.this.getApplicationContext();
                if(alarm != null){
                    alarm.SetAlarm(context);
                }else{
                    Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
