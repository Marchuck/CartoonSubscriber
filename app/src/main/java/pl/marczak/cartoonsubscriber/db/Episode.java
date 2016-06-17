package pl.marczak.cartoonsubscriber.db;

/**
 * @author Lukasz Marczak
 * @since 07.06.16.
 */
public class Episode {
    public String title;
    public String url;

    public Episode(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public Episode() {
    }

    @Override
    public String toString() {
        return title;
    }
}
