package pl.marczak.cartoonsubscriber.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Lukasz Marczak
 * @since 18.06.16.
 */
public class RealmCartoon extends RealmObject {

    @PrimaryKey
    private String uuid;

    private String lastEpisode;

    private String title;

    private boolean isSubscribed;
    private String info;
    private String url;
    private String imageUrl;
    private long episodesCount;

    public long getEpisodesCount() {
        return episodesCount;
    }

    public void setEpisodesCount(long episodesCount) {
        this.episodesCount = episodesCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLastEpisode() {
        return lastEpisode;
    }

    public void setLastEpisode(String lastEpisode) {
        this.lastEpisode = lastEpisode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RealmCartoon() {
    }

    public RealmCartoon(String uuid, String lastEpisode, String title, boolean isSubscribed, String info, String url, String imageUrl, long episodesCount) {
        this.uuid = uuid;
        this.lastEpisode = lastEpisode;
        this.title = title;
        this.isSubscribed = isSubscribed;
        this.info = info;
        this.url = url;
        this.imageUrl = imageUrl;
        this.episodesCount = episodesCount;
    }
}
