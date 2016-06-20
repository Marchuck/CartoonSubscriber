package pl.marczak.cartoonsubscriber.left_tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tt.whorlviewlibrary.WhorlView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.marczak.cartoonsubscriber.R;
import pl.marczak.cartoonsubscriber.db.Cartoon;
import pl.marczak.cartoonsubscriber.model.CartoonMetaData;
import pl.marczak.cartoonsubscriber.utils.GiphyProvider;
import pl.marczak.cartoonsubscriber.utils.Is;
import pl.marczak.cartoonsubscriber.utils.VerboseSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class CenterCartoonFragment extends Fragment {
    public static final String TAG = CenterCartoonFragment.class.getSimpleName();
    rx.Subscription subscription;


    @BindView(R.id.error_layout)
    RelativeLayout error_layout;
    @BindView(R.id.anime_image_webview)
    WebView cartoonImageView;
    @BindView(R.id.anime_subtitle)
    TextView aboutTextView;
    @BindView(R.id.anime_title)
    TextView titleTextView;

    @BindView(R.id.progress_indicator)
    WhorlView progressIndicator;
    String url;
    String title;

    public CenterCartoonFragment() {
    }

    public static CenterCartoonFragment newInstance(@Nullable Cartoon cartoon) {
        CenterCartoonFragment fragment = new CenterCartoonFragment();
        if (cartoon != null) {
            Bundle bundle = new Bundle();
            Log.d(TAG, "title: " + cartoon.title);
            Log.d(TAG, "url: " + cartoon.url);
            bundle.putString("TITLE", cartoon.title);
            /// String url = "http://www.watchcartoononline.com/" + cartoon.title;
            bundle.putString("URL", cartoon.url);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.url = getArguments().getString("URL");
            this.title = getArguments().getString("TITLE");
        } else {
            Log.i(TAG, "nullable getArguments()");
        }
        Log.d(TAG, "onCreate: " + url);
        Log.d(TAG, "onCreate: " + title);

        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_anime, container, false);
        Log.d(TAG, "onCreateView: ");
        ButterKnife.bind(this, view);
        progressIndicator.start();
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        Is.unsubscribe(subscription);
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Subscribe
    public void onEvent(CartoonMetaData event) {
        Log.d(TAG, "onEvent: CartoonMetaData");
        if (event.title == null || event.about == null|| event.about.trim().isEmpty()) {
            titleTextView.setVisibility(View.GONE);
            aboutTextView.setVisibility(View.GONE);
            progressIndicator.stop();
            progressIndicator.setVisibility(View.GONE);
            cartoonImageView.setVisibility(View.GONE);
            error_layout.setVisibility(View.VISIBLE);

        } else {
            titleTextView.setText(event.title);
            aboutTextView.setText(event.about);
            subscription = GiphyProvider.get(event.title).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new VerboseSubscriber<String>(TAG) {
                        @Override
                        public void onNext(String url) {
                            Log.d(TAG, "onNext: GifDrawable");
                            progressIndicator.stop();
                            progressIndicator.setVisibility(View.GONE);
                            cartoonImageView.setVisibility(View.VISIBLE);
                            cartoonImageView.loadUrl(url);
                        }
                    });
        }
    }
}
