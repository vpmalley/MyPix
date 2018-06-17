package fr.vpm.mypix.flickr.persistence;

import java.util.List;

import fr.vpm.mypix.album.Album;
import io.realm.Realm;

public class RealmFlickrAlbumPersister {

  private static final String KEY_ID = "id";

  public void update(Album album) {
    RealmFlickrAlbum realmFlickrAlbum = new RealmFlickrAlbum(album);
    Realm realm = Realm.getDefaultInstance();
    realm.beginTransaction();
    realm.where(RealmFlickrAlbum.class)
        .equalTo(KEY_ID, realmFlickrAlbum.getId())
        .findAll().deleteAllFromRealm();
    realm.insert(realmFlickrAlbum);
    realm.commitTransaction();
  }

  public void persistIfAbsent(List<Album> albums) {
    Realm realm = Realm.getDefaultInstance();
    realm.beginTransaction();
    for (Album album : albums) {
      RealmFlickrAlbum realmFlickrAlbum = new RealmFlickrAlbum(album);
      RealmFlickrAlbum existingAlbum = realm.where(RealmFlickrAlbum.class)
          .equalTo(KEY_ID, realmFlickrAlbum.getId())
          .findFirst();
      if (existingAlbum == null) {
        realm.insert(realmFlickrAlbum);
      }
    }
    realm.commitTransaction();
  }
}
