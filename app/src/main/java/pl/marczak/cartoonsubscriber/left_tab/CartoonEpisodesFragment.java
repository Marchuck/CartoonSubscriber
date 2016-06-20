package pl.marczak.cartoonsubscriber.left_tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tt.whorlviewlibrary.WhorlView;
import com.vi.swipenumberpicker.OnValueChangeListener;
import com.vi.swipenumberpicker.SwipeNumberPicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.marczak.cartoonsubscriber.R;
import pl.marczak.cartoonsubscriber.db.Cartoon;
import pl.marczak.cartoonsubscriber.db.Episode;
import pl.marczak.cartoonsubscriber.db.EpisodesAdapter;
import pl.marczak.cartoonsubscriber.model.CartoonEpisodes;
import pl.marczak.cartoonsubscriber.utils.Is;


public class CartoonEpisodesFragment extends Fragment {
    public static final String TAG = CartoonEpisodesFragment.class.getSimpleName();
    Callbacks callbacks;

    @BindView(R.id.error_layout)
    RelativeLayout error_layout;

    @BindView(R.id.anime_recycler_view)
    RecyclerView cartoonsRecyclerView;
    @BindView(R.id.progress_indicator)
    WhorlView progressIndicator;

    @BindView(R.id.season_picker)
    SwipeNumberPicker seasonPicker;

    EpisodesAdapter adapter;
    String url;
    String title;
    private OnValueChangeListener valueChangeListener = null;

    public CartoonEpisodesFragment() {
    }

    public static CartoonEpisodesFragment newInstance(@Nullable Cartoon cartoon) {
        CartoonEpisodesFragment fragment = new CartoonEpisodesFragment();
        if (cartoon != null) {
            Bundle bundle = new Bundle();
            Log.d(TAG, "title: " + cartoon.title);
            Log.d(TAG, "url: " + cartoon.url);
            bundle.putString("TITLE", cartoon.title);
            /// String url = "http://www.watchcartoononline.com/" + cartoon.title;
            bundle.putString("URL", cartoon.url);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.url = getArguments().getString("URL");
            this.title = getArguments().getString("TITLE");
        } else {
            Log.i(TAG, "nullable getArguments()");
        }
        Log.d(TAG, "onCreate: " + url);
        Log.d(TAG, "onCreate: " + title);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_anime_episodes, container, false);
        Log.d(TAG, "onCreateView: ");
        ButterKnife.bind(this, view);
        adapter = new EpisodesAdapter();
        cartoonsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        callbacks = (Callbacks) getActivity();
        cartoonsRecyclerView.setAdapter(adapter);
        adapter.connectClickListener(callbacks);
        progressIndicator.start();

        return view;
    }

    @Override
    public void onDestroyView() {
        valueChangeListener = null;
        seasonPicker.setOnValueChangeListener(null);
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Subscribe
    public void onEvent(CartoonEpisodes episodes) {
        Log.d(TAG, "onEvent: ");
        progressIndicator.stop();
        progressIndicator.setVisibility(View.GONE);
        if (episodes.episodes == null) {
            error_layout.setVisibility(View.VISIBLE);
        } else {
            adapter.refreshDataSet(episodes.episodes);
            int maxValue = getMaxSeasonValue(episodes);
            if (maxValue == 1) {
                return;
            }
            if (episodes.isNewEpisode)
            adapter.notifyAboutNewEpisodes();

            seasonPicker.setVisibility(View.VISIBLE);
            seasonPicker.setMinValue(1);

            seasonPicker.setMaxValue(maxValue);
            seasonPicker.setValue(maxValue, false);

            valueChangeListener = (view, oldValue, newValue) -> {
                seasonPicker.setValue(newValue, false);
                adapter.refreshAccordingToSeason(newValue);
                return false;
            };
            seasonPicker.setOnValueChangeListener(valueChangeListener);
        }
    }

    private int getMaxSeasonValue(CartoonEpisodes episodes) {
        if (Is.nullable(episodes.episodes)) return 1;
        String[] splits = episodes.episodes.get(0).title.split(" ");
        if (splits.length > 1)
            for (int j = 0; j < splits.length; j++) {
                if (splits[j].toLowerCase().contains("season"))
                    return Integer.valueOf(splits[j + 1]);
            }
        return 1;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface Callbacks {
        void onEpisodeSelected(Episode episode);
    }
}
