package fr.vpm.mypix.album;

import java.util.List;

public class AlbumDisplay {

  private final String name;
  private final Picture picture;
  private final List<Album> albums;

  public AlbumDisplay(String name, Picture picture, List<Album> albums) {
    this.name = name;
    this.picture = picture;
    this.albums = albums;
  }

  public String getName() {
    return name;
  }

  public Picture getPicture() {
    return picture;
  }

  public List<Album> getAlbums() {
    return albums;
  }
}
