package fr.vpm.mypix.album;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

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

  @NonNull
  @Override
  public String getExtension() {
    int dotIndex = path.lastIndexOf('.');
    if (dotIndex > -1 && dotIndex + 1 < path.length()) {
      return path.substring(dotIndex + 1);
    } else {
      return "";
    }
  }

  @Override
  public String getFileName() {
    int slashIndex = path.lastIndexOf('/');
    if (slashIndex > -1 && slashIndex + 1 < path.length()) {
      int extensionLength = getExtension().isEmpty() ? 0 : getExtension().length() + 1;
      return path.substring(slashIndex + 1, path.length() - extensionLength);
    } else {
      return displayName;
    }
  }
}
