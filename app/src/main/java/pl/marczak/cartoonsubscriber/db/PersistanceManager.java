package pl.marczak.cartoonsubscriber.db;

import android.content.Context;
import android.util.Log;

import java.util.UUID;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import pl.marczak.cartoonsubscriber.model.CartoonEntity;
import pl.marczak.cartoonsubscriber.utils.Is;

/**
 * @author Lukasz Marczak
 * @since 18.06.16.
 */
public class PersistanceManager {
public static final String TAG = PersistanceManager.class.getSimpleName();
    public static String uuid() {
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

    public static boolean checkIfExists(Context ctx, CartoonEntity entity) {
        Log.d(TAG, "checkIfExists: ");
        Realm realm = Realm.getInstance(ctx);
        boolean newEpisodeExists = false;

        RealmCartoon cartoons = realm.where(RealmCartoon.class).contains("title", entity.title, Case.INSENSITIVE).findFirst();
        int fixedEpisodesCount = Is.nullable(entity.episodes) ? 0 : entity.episodes.size();
        realm.beginTransaction();
        if (cartoons == null) {
            RealmCartoon cartoon = new RealmCartoon();
            cartoon.setUuid(uuid());
            cartoon.setInfo(entity.about);
            cartoon.setSubscribed(false);
            cartoon.setTitle(entity.title);
            cartoon.setImageUrl(entity.imageUrl == null ? "null" : entity.imageUrl);
            cartoon.setUrl(entity.url == null ? "null" : entity.url);
            cartoon.setEpisodesCount(fixedEpisodesCount);
            newEpisodeExists = true;
        } else {
            if (cartoons.getEpisodesCount() < fixedEpisodesCount) {
                cartoons.setEpisodesCount(fixedEpisodesCount);
                newEpisodeExists = true;
            }
        }
        realm.commitTransaction();
        realm.close();
        return newEpisodeExists;
    }
}
