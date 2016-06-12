package pl.marczak.cartoonsubscriber.left_tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.marczak.cartoonsubscriber.R;


public class LeftNavigatorFragment extends Fragment {
    public static final String TAG = LeftNavigatorFragment.class.getSimpleName();

    public LeftNavigatorFragment() {
    }

    public static LeftNavigatorFragment newInstance() {
        LeftNavigatorFragment fragment = new LeftNavigatorFragment();
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

        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }
}
