package fr.vpm.mypix.album;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.Date;

/**
 * Created by vince on 26/02/18.
 */

public class LocalPicture implements PictureWithUri {

  private final long id;
  private final String path;
  private final String displayName;
  private final Date additionDate;

  public LocalPicture(long id, String path, String displayName, Date additionDate) {
    this.id = id;
    this.path = path;
    this.displayName = displayName;
    this.additionDate = additionDate;
  }

  public long getId() {
    return id;
  }

  public Uri getUri() {
    return ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
  }

  public String getPath() {
    return path;
  }

  public String getDisplayName() {
    return displayName;
  }

  public Date getAdditionDate() {
    return additionDate;
  }
}
