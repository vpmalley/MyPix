package fr.vpm.mypix.flickr.persistence;

import fr.vpm.mypix.album.Album;
import io.realm.Realm;

public class RealmFlickrAlbumPersister {

  public void persist(Album album) {
    RealmFlickrAlbum realmFlickrAlbum = new RealmFlickrAlbum(album);
    Realm realm = Realm.getDefaultInstance();
    realm.beginTransaction();
    realm.insert(realmFlickrAlbum);
    realm.commitTransaction();
  }

}
