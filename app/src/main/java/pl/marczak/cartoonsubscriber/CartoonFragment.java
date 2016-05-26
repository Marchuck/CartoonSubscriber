package pl.marczak.cartoonsubscriber;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.regex.Pattern;

import pl.marczak.cartoonsubscriber.cartoononline.Cartoon;
import rx.functions.Action1;


public class CartoonFragment extends Fragment {
    public static final String TAG = CartoonFragment.class.getSimpleName();
    private OnListFragmentInteractionListener mListener;
    RecyclerView recyclerView;
    SearchView searchView;
    private SearchView.OnQueryTextListener txtChangeListener;

    public CartoonFragment() {
    }

    @SuppressWarnings("unused")
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

        // Set the adapter
        searchView = (SearchView) view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(txtChangeListener);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        final MyCartoonRecyclerViewAdapter adapter = new MyCartoonRecyclerViewAdapter(mListener);
        recyclerView.setAdapter(adapter);
        searchView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        RxExtensions.resultsOnUi(AllCartoonsProvider.getCartoons()).subscribe(new Action1<List<Cartoon>>() {
            @Override
            public void call(List<Cartoon> cartoons) {
                for (Cartoon c : cartoons) Log.i(TAG, "next cartoon: " + c);
                searchView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);

                adapter.refresh(cartoons);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(TAG, "call: " + throwable.getMessage());
                throwable.printStackTrace();
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        txtChangeListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    String firstChar = String.valueOf(newText.charAt(0));
                    if (Pattern.matches("[a-zA-Z]", firstChar)) {
                        Log.d(TAG, "onQueryTextChange: " + newText);
                    }
                }
                return false;
            }
        };
        mListener = new OnListFragmentInteractionListener() {
            @Override
            public void onItemSelected(String url) {

            }
        };
    }

    @Override
    public void onDetach() {
        super.onDetach();
        txtChangeListener = null;
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onItemSelected(String url);
    }
}
