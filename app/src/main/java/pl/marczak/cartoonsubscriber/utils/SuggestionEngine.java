package pl.marczak.cartoonsubscriber.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author Lukasz Marczak
 * @since 17.06.16.
 */
public class SuggestionEngine<T extends Stringable> {
    public static final String TAG = SuggestionEngine.class.getSimpleName();
    private SearchView searchView;
    private List<T> data;
    @NonNull
    private ResultCallback<T> resultCallback;

    public SuggestionEngine(SearchView searchView, List<T> data) {
        this.searchView = searchView;
        this.data = data;
    }

    public static <T extends Stringable> SuggestionEngine connect(SearchView s, List<T> data) {
        return new SuggestionEngine<>(s, data);
    }

    public SuggestionEngine<T> afterSuggest(@NonNull ResultCallback<T> resultCallback) {
        this.resultCallback = resultCallback;
        return this;
    }

    public void init() {
        CartoonSuggestionsEngine.emitInputs(searchView).map(new Func1<String, List<T>>() {
            @Override
            public List<T> call(String s) {
                List<T> out = new ArrayList<>();
                for (T t : data) if (t.containsSubstring(s.toLowerCase())) out.add(t);
                return out;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new VerboseSubscriber<List<T>>(TAG) {
                    @Override
                    public void onNext(List<T> ts) {
                        resultCallback.onSuggested(ts);
                    }
                });
    }

    public interface ResultCallback<T> {
        void onSuggested(List<T> suggestions);
    }
}
