package pl.marczak.cartoonsubscriber.utils;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * @author Lukasz Marczak
 * @since 18.06.16.
 */
public class GiphyProvider {
    public static final String TAG = GiphyProvider.class.getSimpleName();

    public interface GiphyAPI {
        String endpoint = "http://api.giphy.com/v1/gifs";
        String api_key = "dc6zaTOxFJmzC";

        @GET("/search")
        rx.Observable<GiphyResponse> getGif(@Query("q") String query, @Query("api_key") String api_key);
    }

    public static class GiphyResponse {
        public List<GiphyData> data = new ArrayList<>();
    }

    public static class GiphyData {
        public Images images;
    }

    public static class Images {
        public FixedHeight fixed_height;
    }

    public static class FixedHeight {
        public String url;
    }

    public static rx.Observable<String> get(String query) {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(GiphyAPI.endpoint)
                .setConverter(new GsonConverter(new Gson()))
                .build();
        return adapter.create(GiphyAPI.class).getGif(query, GiphyAPI.api_key)
                .map(giphyResponse ->
                        giphyResponse.data.get(shuffled(giphyResponse.data)).images.fixed_height.url);
    }

    private static int shuffled(List<GiphyData> data) {
        return new Random().nextInt(data.size());
    }
}
