package pl.marczak.cartoonsubscriber.db;

/**
 * @author Lukasz Marczak
 * @since 08.06.16.
 */
public class Cartoon {
    public String title;
    public String info;
    public String url;
    public String last_episode;

    public Cartoon(String title, String info, String url, String last_episode) {
        this.title = title;
        this.info = info;
        this.url = url;
        this.last_episode = last_episode;
    }
}
