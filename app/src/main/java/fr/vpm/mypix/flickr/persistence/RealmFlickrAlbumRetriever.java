package fr.vpm.mypix.flickr.persistence;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import fr.vpm.mypix.album.Album;
import fr.vpm.mypix.album.FlickrPicture;
import io.realm.Realm;
import io.realm.RealmResults;

public class RealmFlickrAlbumRetriever {

  private static final String ALBUM_ID = "id";

  @Nullable
  public List<Album> retrieveAllAlbums() {
    Realm realm = Realm.getDefaultInstance();
    RealmResults<RealmFlickrAlbum> flickrAlbums = realm
        .where(RealmFlickrAlbum.class)
        .findAll();
    List<Album> allAlbums = new ArrayList<>();
    for (RealmFlickrAlbum flickrAlbum : flickrAlbums) {
      allAlbums.add(map(flickrAlbum));
    }
    return allAlbums;
  }

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
    Album album = new Album(flickrAlbum.getId(),
        flickrAlbum.getName(),
        flickrAlbum.getDetails(),
        Album.Source.FLICKR,
        flickrAlbum.getPicturesCount());
    for (FlickrPicture flickrPicture : flickrAlbum.getPictures()) {
      album.addPicture(flickrPicture);
    }
    return album;
  }
}
