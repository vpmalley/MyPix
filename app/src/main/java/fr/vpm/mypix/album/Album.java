package fr.vpm.mypix.album;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Album {

  private final Source source;

  private final String id;
  private final String name;
  private final String details;
  private final List<Picture> pictures;
  private final int picturesCount;

  public Album(String id, String name, String details, Source source, int picturesCount) {
    this.id = id;
    this.name = name;
    this.details = details;
    this.source = source;
    this.picturesCount = picturesCount;
    this.pictures = new ArrayList<>();
  }

  public Source getSource() {
    return source;
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

  public int getPicturesCount() {
    return picturesCount;
  }

  public enum Source {
    LOCAL,
    FLICKR
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
