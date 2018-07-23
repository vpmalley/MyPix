package fr.vpm.mypix.album;

import android.support.annotation.NonNull;

import io.realm.RealmObject;

/**
 * Created by vince on 31/03/18.
 */

public class FlickrPicture extends RealmObject implements PictureWithUrl {

  private String urlSmall;
  private String urlMedium;
  private String urlOriginal;
  private String displayName;

  public FlickrPicture() {
  }

  public FlickrPicture(String urlSmall, String urlMedium, String urlOriginal, String displayName) {
    this.urlSmall = urlSmall;
    this.urlMedium = urlMedium;
    this.urlOriginal = urlOriginal;
    this.displayName = displayName;
  }

  public String getUrlSmall() {
    return urlSmall;
  }

  public String getUrlMedium() {
    return urlMedium;
  }

  public String getUrlOriginal() {
    return urlOriginal;
  }

  public String getDisplayName() {
    return displayName;
  }

  @Override
  public String getThumbnailUrl() {
    return urlSmall;
  }

  @Override
  public String getAlbumThumbnailUrl() {
    return urlMedium;
  }

  @Override
  public String getOriginalUrl() {
    return urlOriginal;
  }

  @NonNull
  @Override
  public String getExtension() {
    int dotIndex = urlOriginal.lastIndexOf('.');
    if (dotIndex > -1 && dotIndex + 1 < urlOriginal.length()) {
      return urlOriginal.substring(dotIndex + 1);
    } else {
      return "";
    }
  }

  @Override
  public String getFileName() {
    return displayName;
  }
}
