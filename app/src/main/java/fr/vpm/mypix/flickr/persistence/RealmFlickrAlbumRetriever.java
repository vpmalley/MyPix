package fr.vpm.mypix.flickr.persistence;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import fr.vpm.mypix.album.Album;
import io.realm.Realm;

public class RealmFlickrAlbumRetriever {

  private static final String ALBUM_ID = "id";

  @Nullable
  public Album retrieveAlbum(@NonNull String flickrAlbumId) {
    Realm realm = Realm.getDefaultInstance();
    RealmFlickrAlbum flickrAlbum = realm.where(RealmFlickrAlbum.class)
        .equalTo(ALBUM_ID, flickrAlbumId)
        .findFirst();
    return flickrAlbum == null ? null : map(flickrAlbum);
  }

  @NonNull
  private Album map(@NonNull RealmFlickrAlbum flickrAlbum) {
    return new Album(flickrAlbum.getId(),
        flickrAlbum.getName(),
        flickrAlbum.getDetails(),
        Album.Source.FLICKR);
  }
}
