package pl.marczak.cartoonsubscriber;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    FloatingActionButton fab;
    AlarmManagerBroadcastReceiver alarm;


    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        long _12hoursInMillis = TimeUnit.MINUTES.toMillis(5);
        Log.d(TAG, "onCreate: " + _12hoursInMillis);

        alarm = new AlarmManagerBroadcastReceiver();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        setFragmentForPlaceholder(Const.MIDDLE, CartoonFragment.newInstance());

    }

    public void setFragmentForPlaceholder(@DrawerMode int drawerSetting, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (drawerSetting == Const.LEFT) {
            fragmentTransaction.replace(R.id.left_content, fragment);
        } else if (drawerSetting == Const.RIGHT) {
            fragmentTransaction.replace(R.id.right_content, fragment);
        } else {
            fragmentTransaction.replace(R.id.center_content, fragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
    }
}
