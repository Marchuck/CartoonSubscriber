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
//
// InputStream sourceIs = getResources().openRawResource(R.raw.logo);
//        BufferedInputStream bis = null;
//        try {
//            bis = new BufferedInputStream(sourceIs, sourceIs.available());
//            gifFromStream = new GifDrawable(bis);
//            gifFromStream.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        // Set the adapter
//        searchView = (SearchView) view.findViewById(R.id.search_view);
//
//        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
//        final MyCartoonRecyclerViewAdapter adapter = new MyCartoonRecyclerViewAdapter(mListener);
//        final FakeCartoonAdapter fakeAdapter = new FakeCartoonAdapter(4);
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//        recyclerView.setAdapter(adapter);
//
////        searchView.setVisibility(View.GONE);
////        DBSaver<Cartoon> dbSaver = new CartoonSaver(this.getActivity().getApplicationContext());
//        engine = new CartoonSuggestionsEngine(CartoonFragment.this.getActivity());
//        engine.getCartoons(getActivity().getApplicationContext(),searchView)
//                .subscribeOn(Schedulers.trampoline())
//                .observeOn(AndroidSchedulers.mainThread()).subscribe(new VerboseSubscriber<List<Cartoon>>(TAG) {
//            @Override
//            public void onNext(List<Cartoon> cartoons) {
//                Log.d(TAG, "onNext: "+cartoons.size());
//                adapter.refresh(cartoons);
//            }
//        });

        return view;
    }


    @Override
    public void onDetach() {
        gifFromStream.stop();
        mListener = null;
        super.onDetach();
    }

    public interface OnListFragmentInteractionListener {
        void onItemSelected(String url);
    }
}
