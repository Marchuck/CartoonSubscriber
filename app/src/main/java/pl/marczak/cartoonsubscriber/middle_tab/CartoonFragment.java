package pl.marczak.cartoonsubscriber.middle_tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import pl.marczak.cartoonsubscriber.R;
import pl.marczak.cartoonsubscriber.utils.CartoonSuggestionsEngine;

public class CartoonFragment extends Fragment {
    public static final String TAG = CartoonFragment.class.getSimpleName();
    /**
     * VIEWS
     */
//    @BindView(R.id.recycler_view)
//    RecyclerView recyclerView;//, fakeRecyclerView;
//    @BindView(R.id.search_view)
//    SearchView searchView;

    @BindView(R.id.image)
    ImageView imageView;
    /**
     * HELPERS & LISTENERS & CALLBACKS
     */
    CartoonSuggestionsEngine engine;
    GifDrawable gifFromStream;
    private OnListFragmentInteractionListener mListener;

    public CartoonFragment() {
    }

    public static CartoonFragment newInstance() {
        CartoonFragment fragment = new CartoonFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.center_splash, container, false);
        Log.d(TAG, "onCreateView: ");
        ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface OnListFragmentInteractionListener {
        void onItemSelected(String url);
    }
}
