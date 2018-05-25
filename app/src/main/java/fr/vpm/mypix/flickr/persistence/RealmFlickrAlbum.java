package fr.vpm.mypix.flickr.persistence;

import fr.vpm.mypix.album.Album;
import fr.vpm.mypix.album.FlickrPicture;
import fr.vpm.mypix.album.Picture;
import io.realm.RealmList;
import io.realm.RealmObject;

public class RealmFlickrAlbum extends RealmObject {

  private String id;
  private String name;
  private String details;
  private RealmList<FlickrPicture> pictures;

  public RealmFlickrAlbum() {
  }

  public RealmFlickrAlbum(Album album) {
    this.id = album.getId();
    this.name = album.getName();
    this.details = album.getDetails();
    this.pictures = new RealmList<>();
    for (Picture picture : album.getPictures()) {
      if (picture instanceof FlickrPicture) {
        pictures.add((FlickrPicture) picture);
      }
    }
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDetails() {
    return details;
  }

  public RealmList<FlickrPicture> getPictures() {
    return pictures;
  }
}
