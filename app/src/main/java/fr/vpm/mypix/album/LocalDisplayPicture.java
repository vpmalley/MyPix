package fr.vpm.mypix.album;

import android.net.Uri;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class LocalDisplayPicture implements PictureWithUri {

  private final PictureWithUri displayedPicture;
  private final List<Picture> pictures;

  public LocalDisplayPicture(PictureWithUri displayedPicture) {
    this.pictures = new ArrayList<>();
    this.displayedPicture = displayedPicture;
  }

  public List<Picture> getPictures() {
    return pictures;
  }

  public void addPicture(Picture secondPicture) {
    this.pictures.add(secondPicture);
  }

  public void addPictures(List<Picture> pictures) {
    this.pictures.addAll(pictures);
  }

  @Override
  public Uri getUri() {
    return displayedPicture.getUri();
  }

  @Override
  public String getFileNameWithExtension() {
    return displayedPicture.getFileNameWithExtension();
  }

  @NonNull
  @Override
  public String getExtension() {
    return displayedPicture.getExtension();
  }

  @Override
  public String getFileName() {
    return displayedPicture.getFileName();
  }
}
