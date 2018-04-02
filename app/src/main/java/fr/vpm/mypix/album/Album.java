package fr.vpm.mypix.album;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Album album = (Album) o;
    return Objects.equals(name, album.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  @Override
  public String toString() {
    return name;
  }
}
