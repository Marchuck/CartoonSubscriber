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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.marczak.cartoonsubscriber.R;
import pl.marczak.cartoonsubscriber.db.Cartoon;
import pl.marczak.cartoonsubscriber.db.Episode;
import pl.marczak.cartoonsubscriber.db.EpisodesAdapter;
import pl.marczak.cartoonsubscriber.model.CartoonEpisodes;


public class CurrentAnimeFragment extends Fragment {
    public static final String TAG = CurrentAnimeFragment.class.getSimpleName();
    Callbacks callbacks;

    @BindView(R.id.error_layout)
    RelativeLayout error_layout;

    @BindView(R.id.anime_recycler_view)
    RecyclerView cartoonsRecyclerView;
    @BindView(R.id.progress_indicator)
    WhorlView progressIndicator;
    EpisodesAdapter adapter;
    String url;
    String title;

    public CurrentAnimeFragment() {
    }

    public static CurrentAnimeFragment newInstance(@Nullable Cartoon cartoon) {
        CurrentAnimeFragment fragment = new CurrentAnimeFragment();
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
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface Callbacks {
        void onEpisodeSelected(Episode episode);
    }
}
