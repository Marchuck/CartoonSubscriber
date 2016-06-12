package pl.marczak.cartoonsubscriber;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pl.marczak.cartoonsubscriber.left_tab.LeftNavigatorFragment;
import pl.marczak.cartoonsubscriber.middle_tab.CartoonFragment;
import pl.marczak.cartoonsubscriber.right_tab.RightNavigatorFragment;
import pl.marczak.cartoonsubscriber.utils.Const;
import pl.marczak.cartoonsubscriber.utils.DrawerMode;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    /**
     * VIEWS
     */
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.drawer_layout)
    android.support.v4.widget.DrawerLayout drawerLayout;

    /***
     * HELPERS & LISTENERS & CALLBACKS
     */
    ActionBarDrawerToggle toggle;
    Unbinder unbinder;
    View.OnClickListener fabListener;
    DrawerLayout.DrawerListener drawerListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        ButterKnife.bind(this);
        long _12hoursInMillis = TimeUnit.MINUTES.toMillis(5);
        Log.d(TAG, "onCreate: " + _12hoursInMillis);
        setupViews();
        String query = null;
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
        }
        setFragmentForPlaceholder(Const.MIDDLE, CartoonFragment.newInstance());
        setFragmentForPlaceholder(Const.LEFT, LeftNavigatorFragment.newInstance());
        setFragmentForPlaceholder(Const.RIGHT, RightNavigatorFragment.newInstance(query));
        if (query != null) drawerLayout.openDrawer(Gravity.RIGHT);
    }

    private void setupViews() {

        if (drawerLayout == null) {
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        }
        toggle = new ActionBarDrawerToggle(this, drawerLayout, android.R.string.ok,
                android.R.string.cancel) {
            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        toggle.syncState();
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
    protected void onDestroy() {
        drawerListener = null;
        fabListener = null;
        toggle = null;
        super.onDestroy();
    }
}
