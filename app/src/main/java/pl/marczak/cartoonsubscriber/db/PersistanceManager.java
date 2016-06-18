package pl.marczak.cartoonsubscriber.db;

import android.content.Context;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author Lukasz Marczak
 * @since 18.06.16.
 */
public class PersistanceManager {

    private static String uuid() {
        return UUID.randomUUID().toString();
    }

    private static String millis() {
        return String.valueOf(System.currentTimeMillis());
    }

    public static void updateLastTimestamp(Context ctx) {
        Realm realm = Realm.getInstance(ctx);
        realm.beginTransaction();
        RealmResults<RealmTimestamp> lastTimeStamp = realm.where(RealmTimestamp.class)
                .findAll();
        RealmTimestamp timestamp;
        if (lastTimeStamp == null || lastTimeStamp.isEmpty()) {
            timestamp = new RealmTimestamp();
            timestamp.setUuid(uuid());
            timestamp.setTimestamp(millis());
        } else {
            timestamp = lastTimeStamp.first();
            timestamp.setTimestamp(millis());
        }
        realm.copyToRealmOrUpdate(timestamp);
        realm.commitTransaction();
        realm.close();
    }
}
