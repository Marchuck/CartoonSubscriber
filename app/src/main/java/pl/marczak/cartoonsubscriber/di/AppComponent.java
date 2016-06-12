package pl.marczak.cartoonsubscriber.di;

import javax.inject.Singleton;

import dagger.Component;
import pl.marczak.cartoonsubscriber.MainActivity;
import pl.marczak.cartoonsubscriber.db.CartoonsProvider;

/**
 * @author Lukasz Marczak
 * @since 08.06.16.
 */
@Component(modules = AppModule.class)
@Singleton
public interface AppComponent {

    MainActivity inject(MainActivity activity);

    CartoonsProvider inject(CartoonsProvider provider);
}