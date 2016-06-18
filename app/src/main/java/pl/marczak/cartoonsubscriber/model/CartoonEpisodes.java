package pl.marczak.cartoonsubscriber.model;

import java.util.List;

import pl.marczak.cartoonsubscriber.db.Episode;

/**
 * @author Lukasz Marczak
 * @since 18.06.16.
 */
public class CartoonEpisodes {
    public List<Episode> episodes;

    public CartoonEpisodes() {
    }

    public CartoonEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }
}
