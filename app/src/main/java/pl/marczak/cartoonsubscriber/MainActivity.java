package pl.marczak.cartoonsubscriber;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.marczak.cartoonsubscriber.db.Cartoon;
import pl.marczak.cartoonsubscriber.db.Episode;
import pl.marczak.cartoonsubscriber.left_tab.CenterCartoonFragment;
import pl.marczak.cartoonsubscriber.left_tab.CurrentAnimeFragment;
import pl.marczak.cartoonsubscriber.middle_tab.CartoonFragment;
import pl.marczak.cartoonsubscriber.model.CartoonEntity;
import pl.marczak.cartoonsubscriber.model.CartoonEpisodes;
import pl.marczak.cartoonsubscriber.model.CartoonMetaData;
import pl.marczak.cartoonsubscriber.net.ApiRequest;
import pl.marczak.cartoonsubscriber.right_tab.RightNavigatorFragment;
import pl.marczak.cartoonsubscriber.utils.Const;
import pl.marczak.cartoonsubscriber.utils.DrawerMode;
import pl.marczak.cartoonsubscriber.utils.VerboseSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements RightNavigatorFragment.Callbacks,
        CurrentAnimeFragment.Callbacks {
    public static final String TAG = MainActivity.class.getSimpleName();
    /**
     * VIEWS
     */
    @BindView(R.id.drawer_layout)
    android.support.v4.widget.DrawerLayout drawerLayout;

    /***
     * HELPERS & LISTENERS & CALLBACKS
     */
    ActionBarDrawerToggle toggle;

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
        setFragmentForPlaceholder(Const.LEFT, CurrentAnimeFragment.newInstance(null));
        setFragmentForPlaceholder(Const.RIGHT, RightNavigatorFragment.newInstance(query));
        if (query != null) drawerLayout.openDrawer(Gravity.RIGHT);
        EventBus.getDefault().register(this);
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
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onRightItemSelected(Cartoon cartoon) {
        Log.d(TAG, "onRightItemSelected: " + cartoon);
        drawerLayout.closeDrawer(Gravity.RIGHT);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        CurrentAnimeFragment left = CurrentAnimeFragment.newInstance(cartoon);
        CenterCartoonFragment middle = CenterCartoonFragment.newInstance(cartoon);
        setFragmentForPlaceholder(Const.LEFT, left);
        setFragmentForPlaceholder(Const.MIDDLE, middle);
        drawerLayout.openDrawer(Gravity.LEFT);
        ApiRequest.create()
                .getEpisodesWithData(cartoon.url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new VerboseSubscriber<CartoonEntity>(TAG) {
            @Override
            public void onNext(CartoonEntity entity) {
                Log.d(TAG, "onNext: ");
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                EventBus.getDefault().post(new CartoonMetaData(entity.about, entity.imageUrl, cartoon.title));
                EventBus.getDefault().post(new CartoonEpisodes(entity.episodes));
                drawerLayout.openDrawer(Gravity.LEFT);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    @Subscribe
    public void onEvent(Object s){
        Log.d(TAG, "onEvent: object");
    }

    @Override
    public void onEpisodeSelected(Episode episode) {
        Log.d(TAG, "onEpisodeSelected: ");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(episode.url));
        startActivity(browserIntent);
    }
}
