package fr.vpm.mypix.album;

import java.util.ArrayList;
import java.util.List;

/**
 * A dummy item representing a piece of name.
 */
public class Album {
  private final String id;
  private final String name;
  private final String details;
  private final List<Picture> pictures;

  public Album(String id, String name, String details) {
    this.id = id;
    this.name = name;
    this.details = details;
    this.pictures = new ArrayList<>();
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

  public List<Picture> getPictures() {
    return pictures;
  }

  public void addPicture(final Picture picture) {
    pictures.add(picture);
  }

  @Override
  public String toString() {
    return name;
  }
}
