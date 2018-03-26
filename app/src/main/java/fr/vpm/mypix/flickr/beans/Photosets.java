package fr.vpm.mypix.flickr.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
class Photosets {
  private int total;
  private int perpage;
  private List<Photoset> photoset;
  private int page;
  private int pages;

  public int getTotal() {
    return total;
  }

  public int getPerpage() {
    return perpage;
  }

  public List<Photoset> getPhotoset() {
    return photoset;
  }

  public int getPage() {
    return page;
  }

  public int getPages() {
    return pages;
  }
}