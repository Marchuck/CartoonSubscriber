package pl.marczak.cartoonsubscriber.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Lukasz Marczak
 * @since 18.06.16.
 */
public class RealmTimestamp extends RealmObject{

    @PrimaryKey
    private String uuid;

    private String timestamp;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public RealmTimestamp() {
    }
}
