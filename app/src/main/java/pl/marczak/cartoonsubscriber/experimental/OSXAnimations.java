package pl.marczak.cartoonsubscriber.experimental;

import android.support.annotation.NonNull;
import android.view.View;

import pl.marczak.cartoonsubscriber.utils.WeakHandler;

/**
 * @author Lukasz Marczak
 * @since 20.06.16.
 */
public class OSXAnimations {
    public interface EndCallback extends Runnable {

    }
//    public static class Handlers {
//        public WeakHandler weakHandler;
//        public Runnable runnable;
//
//        public Handlers(Runnable runnable, WeakHandler weakHandler) {
//            this.runnable = runnable;
//            this.weakHandler = weakHandler;
//        }
//
//        public void dispose() {
//            weakHandler.removeCallbacks(runnable);
//            runnable = null;
//            weakHandler = null;
//        }
//    }

    public static void onViewClickAnimation(View view, int duration, float endZoom, @NonNull EndCallback endCallback) {
        WeakHandler weakHandler = new WeakHandler();
        view.animate().alpha(0.4f).scaleX(endZoom).scaleY(endZoom).setDuration(duration).start();

        weakHandler.postDelayed(endCallback, 2 * duration);
        weakHandler.postDelayed(() ->
                view.animate().alpha(1).scaleX(1f).scaleY(1f).setDuration(duration).start(), duration);
    }
}
