package pl.marczak.cartoonsubscriber.middle_tab;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.marczak.cartoonsubscriber.R;
import pl.marczak.cartoonsubscriber.experimental.OSXAnimations;
import pl.marczak.cartoonsubscriber.utils.CartoonSuggestionsEngine;

public class CartoonFragment extends Fragment {
    public static final String TAG = CartoonFragment.class.getSimpleName();
    /**
     * VIEWS
     */
    @BindView(R.id.image)
    ImageView imageView;
    /**
     * HELPERS & LISTENERS & CALLBACKS
     */
    CartoonSuggestionsEngine engine;
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

        imageView.setOnClickListener(v -> {
            OSXAnimations.onViewClickAnimation(imageView, 120, 1.4f, () -> {
                Snackbar.make(getView(), "Open menu on right", Snackbar.LENGTH_SHORT).show();
            });
        });

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
