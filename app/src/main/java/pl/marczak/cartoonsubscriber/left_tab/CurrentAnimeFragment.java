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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.marczak.cartoonsubscriber.R;
import pl.marczak.cartoonsubscriber.db.Cartoon;
import pl.marczak.cartoonsubscriber.db.Episode;
import pl.marczak.cartoonsubscriber.db.EpisodesAdapter;
import pl.marczak.cartoonsubscriber.model.CartoonEntity;
import pl.marczak.cartoonsubscriber.net.ApiRequest;
import pl.marczak.cartoonsubscriber.utils.VerboseSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class CurrentAnimeFragment extends Fragment {
    public static final String TAG = CurrentAnimeFragment.class.getSimpleName();
    Callbacks callbacks;
    @BindView(R.id.anime_recycler_view)
    RecyclerView cartoonsRecyclerView;
    @BindView(R.id.anime_image)
    ImageView cartoonImageView;
    @BindView(R.id.anime_subtitle)
    TextView aboutTextView;
    @BindView(R.id.anime_title)
    TextView titleTextView;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_anime, container, false);
        Log.d(TAG, "onCreateView: ");
        ButterKnife.bind(this, view);
        adapter = new EpisodesAdapter();
        cartoonsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        callbacks = (Callbacks) getActivity();
        cartoonsRecyclerView.setAdapter(adapter);
        adapter.connectClickListener(callbacks);
        ApiRequest.create()
                .getEpisodesWithData(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new VerboseSubscriber<CartoonEntity>(TAG) {
                    @Override
                    public void onNext(CartoonEntity entity) {
                        Log.d(TAG, "onNext: " + entity.toString());
                        try {
                            Picasso.with(getActivity())
                                    .load(entity.imageUrl)
                                    .into(cartoonImageView);
                            aboutTextView.setText(entity.about);
                            titleTextView.setText(title);
                            adapter.refreshDataSet(entity.episodes);
                        } catch (Exception ignored) {
                            Log.e(TAG, "onNext: Error propagated from onNext");
                            onError(new Throwable("xDxxdxdxdxd"));
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        // super.onError(e);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "runOnUiThread: ");
                                titleTextView.setText("Oops! Something went wrong");
                            }
                        });
                    }
                });
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface Callbacks {
        void onEpisodeSelected(Episode episode);
    }
}
