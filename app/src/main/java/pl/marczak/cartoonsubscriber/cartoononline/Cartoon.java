package pl.marczak.cartoonsubscriber.cartoononline;

/**
 * @author Lukasz Marczak
 * @since 26.05.16.
 */
public class Cartoon {
    public String name;



    public String url;

    public Cartoon() {
    }

    public Cartoon(String name, String url) {
        this.name = name;
        this.url = url;
    }
    @Override
    public String toString() {
        return
                "name: " + name +
                        ", url: " + url;
    }
}
