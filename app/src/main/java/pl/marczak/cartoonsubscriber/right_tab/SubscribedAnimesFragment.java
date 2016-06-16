package pl.marczak.cartoonsubscriber.right_tab;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.marczak.cartoonsubscriber.R;


public class SubscribedAnimesFragment extends Fragment {

    public SubscribedAnimesFragment() {
        // Required empty public constructor
    }

    public static SubscribedAnimesFragment newInstance( ) {
        SubscribedAnimesFragment fragment = new SubscribedAnimesFragment();
          return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_subscribed_animes, container, false);
        return view;
    }

}
