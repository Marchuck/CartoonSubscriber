package pl.marczak.cartoonsubscriber.db;

import pl.marczak.cartoonsubscriber.utils.Stringable;

/**
 * @author Lukasz Marczak
 * @since 08.06.16.
 */
public class Cartoon implements Stringable {
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

    @Override
    public String toString() {
        return "Cartoon{" +
                "title='" + title + '\'' +
                ", info='" + info + '\'' +
                ", url='" + url + '\'' +
                ", last_episode='" + last_episode + '\'' +
                '}';
    }

    @Override
    public boolean containsSubstring(Object o) {
        return o instanceof String && title.toLowerCase().contains(((String) o).toLowerCase());
    }
}
