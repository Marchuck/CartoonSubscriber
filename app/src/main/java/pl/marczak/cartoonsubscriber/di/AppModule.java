package pl.marczak.cartoonsubscriber.di;

/**
 * @author Lukasz Marczak
 * @since 08.06.16.
 */

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.marczak.cartoonsubscriber.R;
import pl.marczak.cartoonsubscriber.db.CartoonDbHelper;

/**
 * @author Lukasz Marczak
 * @since 08.06.16.
 */
@Module
public class AppModule {
    private App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(app);
    }

    @Provides
    @Singleton
    public MediaPlayer provideMediaPlayer() {
        return MediaPlayer.create(app, R.raw.win95);
    }

    @Provides
    @Singleton
    public CartoonDbHelper provideCartoonDbHelper() {
        return new CartoonDbHelper(app.getApplicationContext());
    }


}
