package fr.vpm.mypix.flickr.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Photos {

  private String total;
  private List<Photo> photo;
  private int perpage;
  private int page;
  private int pages;

  public String getTotal() {
    return total;
  }

  public List<Photo> getPhoto() {
    return photo;
  }

  public int getPerpage() {
    return perpage;
  }

  public int getPage() {
    return page;
  }

  public int getPages() {
    return pages;
  }
}