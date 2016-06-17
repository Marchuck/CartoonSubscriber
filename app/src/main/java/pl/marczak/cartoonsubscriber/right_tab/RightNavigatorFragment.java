package pl.marczak.cartoonsubscriber.right_tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.marczak.cartoonsubscriber.R;
import pl.marczak.cartoonsubscriber.db.Cartoon;
import pl.marczak.cartoonsubscriber.db.CartoonAdapter;
import pl.marczak.cartoonsubscriber.net.AllCartoonsProvider;
import pl.marczak.cartoonsubscriber.utils.Is;
import pl.marczak.cartoonsubscriber.utils.SuggestionEngine;
import pl.marczak.cartoonsubscriber.utils.VerboseSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class RightNavigatorFragment extends Fragment {
    public static final String TAG = RightNavigatorFragment.class.getSimpleName();
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.search_view)
    android.support.v7.widget.SearchView searchView;
    CartoonAdapter cartoonAdapter;
    private Callbacks callbacks;
    @Nullable
    private String query;
    private List<Cartoon> fullData;
    public RightNavigatorFragment() {
    }

    public static RightNavigatorFragment newInstance(@Nullable String query) {
        Log.d(TAG, "newInstance: ");
        RightNavigatorFragment fragment = new RightNavigatorFragment();
        if (query != null) {
            Bundle b = new Bundle();
            b.putString("QUERY", query);
            fragment.setArguments(b);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            this.query = savedInstanceState.getString("QUERY");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_cartoon_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated: ");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cartoonAdapter = new CartoonAdapter();
        recyclerView.setAdapter(cartoonAdapter);
        callbacks = (Callbacks) getActivity();
        cartoonAdapter.connectClickListener(callbacks);

        AllCartoonsProvider.getCartoons().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new VerboseSubscriber<List<Cartoon>>(TAG) {
                    @Override
                    public void onNext(List<Cartoon> cartoons) {
                        Log.d(TAG, "onNext: nullable? " + Is.humanReads(Is.nullable(cartoons)));
                        fullData = cartoons;
                        cartoonAdapter.setDataSet(fullData);
                        recyclerView.setVisibility(View.VISIBLE);
                        SuggestionEngine<Cartoon> engine = new SuggestionEngine<>(searchView, fullData);
                        engine.afterSuggest(new SuggestionEngine.ResultCallback<Cartoon>() {
                            @Override
                            public void onSuggested(List<Cartoon> suggestions) {
                                Log.d(TAG, "onSuggested: " + suggestions.size());
                                cartoonAdapter.setDataSet(suggestions);
                            }
                        }).init();
                    }
                });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface Callbacks {
        void onRightItemSelected(Cartoon cartoon);
    }
}
