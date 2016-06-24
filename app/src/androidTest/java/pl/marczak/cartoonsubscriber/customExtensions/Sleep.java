package pl.marczak.cartoonsubscriber.customExtensions;

/**
 * @author Lukasz Marczak
 * @since 24.06.16.
 */
public class Sleep {
    public Sleep(long millis){
        try {
            Thread.sleep(millis);
        }catch (InterruptedException ex){

        }
    }
}
