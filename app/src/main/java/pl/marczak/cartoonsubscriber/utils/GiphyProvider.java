package pl.marczak.cartoonsubscriber.utils;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

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
                        giphyResponse.data.get(0).images.fixed_height.url);
    }

    public static rx.Observable<GifDrawable> getGif(String query) {
        Log.d(TAG, "getGif: ");
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(GiphyAPI.endpoint)
                .setConverter(new GsonConverter(new Gson()))
                .build();
        return adapter.create(GiphyAPI.class).getGif("cat", GiphyAPI.api_key)
                .flatMap(giphyResponse -> getGifDrawable(giphyResponse.data.get(0).images.fixed_height.url));
    }

    public static Observable<GifDrawable> getGifDrawable(String url) {
        Log.d(TAG, "getGifDrawable: " + url);
        return Observable.create((Observable.OnSubscribe<GifDrawable>) subscriber -> {
            URLConnection theConnection;
            try {
                theConnection = new URL(url).openConnection();
                //  theConnection.setRequestProperty("Accept-Charset", "UTF-8");

                HttpURLConnection httpConn = (HttpURLConnection) theConnection;

                //  int responseCode = httpConn.getResponseCode();
                //  String responseMessage = httpConn.getResponseMessage();

                InputStream is = null;
                is = httpConn.getInputStream();

                int length = getBytes(is).length;
                Log.d(TAG, "getGifDrawable: " + length);
                BufferedInputStream bis = new BufferedInputStream(is, length < 1024 ? 1024 * 5 : length);
                GifDrawable gifFromStream = new GifDrawable(bis);

                subscriber.onNext(gifFromStream);
                subscriber.onCompleted();
                is.close();

            } catch (IOException e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
        });
    }

    public static byte[] getBytes(InputStream is) throws IOException {

        int len;
        int size = 1024;
        byte[] buf;

        if (is instanceof ByteArrayInputStream) {
            size = is.available();
            buf = new byte[size];
            len = is.read(buf, 0, size);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            buf = new byte[size];
            while ((len = is.read(buf, 0, size)) != -1)
                bos.write(buf, 0, len);
            buf = bos.toByteArray();
        }
        return buf;
    }
}
