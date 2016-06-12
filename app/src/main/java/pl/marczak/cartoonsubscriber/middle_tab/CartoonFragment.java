package pl.marczak.cartoonsubscriber.middle_tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.marczak.cartoonsubscriber.R;
import pl.marczak.cartoonsubscriber.db.Cartoon;
import pl.marczak.cartoonsubscriber.utils.CartoonSuggestionsEngine;
import pl.marczak.cartoonsubscriber.utils.VerboseSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CartoonFragment extends Fragment {
    public static final String TAG = CartoonFragment.class.getSimpleName();
    private OnListFragmentInteractionListener mListener;
    /**
     * VIEWS
     */
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;//, fakeRecyclerView;
    @BindView(R.id.search_view)
    SearchView searchView;
    /**
     * HELPERS & LISTENERS & CALLBACKS
     */
    CartoonSuggestionsEngine engine;

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
        View view = inflater.inflate(R.layout.fragment_cartoon_list, container, false);
        Log.d(TAG, "onCreateView: ");
        ButterKnife.bind(this, view);
        // Set the adapter
        searchView = (SearchView) view.findViewById(R.id.search_view);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        final MyCartoonRecyclerViewAdapter adapter = new MyCartoonRecyclerViewAdapter(mListener);
        final FakeCartoonAdapter fakeAdapter = new FakeCartoonAdapter(4);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(adapter);

//        searchView.setVisibility(View.GONE);
//        DBSaver<Cartoon> dbSaver = new CartoonSaver(this.getActivity().getApplicationContext());
        engine = new CartoonSuggestionsEngine(CartoonFragment.this.getActivity());
        engine.getCartoons(getActivity().getApplicationContext(),searchView)
                .subscribeOn(Schedulers.trampoline())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new VerboseSubscriber<List<Cartoon>>() {
            @Override
            public void onNext(List<Cartoon> cartoons) {
                Log.d(TAG, "onNext: "+cartoons.size());
                adapter.refresh(cartoons);
            }
        });
//        RxExtensions.resultsOnUi(AllCartoonsProvider.fetchCartoons(dbSaver))
//                .subscribe(new Action1<List<Cartoon>>() {
//            @Override
//            public void call(List<Cartoon> cartoons) {
//                 for (Cartoon c : cartoons) Log.i(TAG, "next cartoon: " + c);
//                adapter.refresh(cartoons.subList(0,10));
//                engine = new CartoonSuggestionsEngine(CartoonFragment.this.getActivity());
//                engine.getCartoons(searchView)
//                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new VerboseSubscriber<List<Cartoon>>() {
//                    @Override
//                    public void onNext(List<Cartoon> cartoons) {
//                        Log.d(TAG, "onNext: "+cartoons.size());
//                        adapter.refresh(cartoons);
//                    }
//                });
//                recyclerView.setVisibility(View.VISIBLE);
//
//
//            }
//        }, new Action1<Throwable>() {
//            @Override
//            public void call(Throwable throwable) {
//                Log.e(TAG, "call: " + throwable.getMessage());
//                throwable.printStackTrace();
//            }
//        });

        return view;
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onItemSelected(String url);
    }
}
