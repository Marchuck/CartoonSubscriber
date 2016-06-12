package pl.marczak.cartoonsubscriber.right_tab;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pl.marczak.cartoonsubscriber.R;
import pl.marczak.cartoonsubscriber.db.Cartoon;
import pl.marczak.cartoonsubscriber.db.CartoonAdapter;
import pl.marczak.cartoonsubscriber.db.CartoonsLoader;


public class RightNavigatorFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Cartoon>> {
    public static final String TAG = RightNavigatorFragment.class.getSimpleName();

   // @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    CartoonAdapter cartoonAdapter;

    @Nullable
    private String query;

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
       // ButterKnife.bind(this, view);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartoonAdapter = new CartoonAdapter();
        recyclerView.setAdapter(cartoonAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Loader<List<Cartoon>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: ");
        CartoonsLoader loader = new CartoonsLoader(this.getContext());
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Cartoon>> loader, List<Cartoon> data) {
        Log.d(TAG, "onLoadFinished: ");
        cartoonAdapter.setDataSet(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Cartoon>> loader) {
        Log.d(TAG, "onLoaderReset: ");
    }
}
