package fr.vpm.mypix.album;

import android.support.annotation.NonNull;

/**
 * Created by vince on 31/03/18.
 */

public class FlickrPicture implements PictureWithUrl {

  private final String urlSmall;
  private final String urlOriginal;
  private final String displayName;

  public FlickrPicture(String urlSmall, String urlOriginal, String displayName) {
    this.urlSmall = urlSmall;
    this.urlOriginal = urlOriginal;
    this.displayName = displayName;
  }

  public String getUrlSmall() {
    return urlSmall;
  }

  public String getUrlOriginal() {
    return urlOriginal;
  }

  public String getDisplayName() {
    return displayName;
  }

  @Override
  public String getUrl() {
    return urlSmall;
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
