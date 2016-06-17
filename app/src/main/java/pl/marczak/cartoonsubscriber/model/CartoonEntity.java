package pl.marczak.cartoonsubscriber.model;

import java.util.ArrayList;
import java.util.List;

import pl.marczak.cartoonsubscriber.db.Episode;

/**
 * @author Lukasz Marczak
 * @since 17.06.16.
 */
public class CartoonEntity {
    public List<Episode> episodes = new ArrayList<>();
    public String about;
    public String imageUrl;
    public String title;

    @Override
    public String toString() {
        return "CartoonEntity{" +
                "episodes=" + episodes.toString() +
                ", about='" + about + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
