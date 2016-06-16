package pl.marczak.cartoonsubscriber.left_tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.marczak.cartoonsubscriber.R;
import pl.marczak.cartoonsubscriber.net.ApiRequest;
import pl.marczak.cartoonsubscriber.utils.RxExtensions;


public class CurrentAnimeFragment extends Fragment {
    public static final String TAG = CurrentAnimeFragment.class.getSimpleName();

    @BindView(R.id.anime_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.anime_image)
    ImageView imageView;

    @BindView(R.id.anime_subtitle)
    TextView subtitle;
    String url;

    public CurrentAnimeFragment() {
    }

    public static CurrentAnimeFragment newInstance() {
        CurrentAnimeFragment fragment = new CurrentAnimeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) this.url = savedInstanceState.getString("URL");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_anime, container, false);
        Log.d(TAG, "onCreateView: ");
        ButterKnife.bind(this, view);
        RxExtensions.resultsOnUi(ApiRequest.create().execute(url)).subscribe();
        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }
}
