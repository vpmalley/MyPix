package fr.vpm.mypix.flickr.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SinglePhotoset {

  private String perpage;
  private String title;
  private List<Photo> photo;
  private int pages;
  private String primary;
  private String id;
  private String ownername;
  private String owner;
  private String per_page;
  private int total;
  private int page;

  public String getPerpage() {
    return perpage;
  }

  public String getTitle() {
    return title;
  }

  public List<Photo> getPhoto() {
    return photo;
  }

  public int getPages() {
    return pages;
  }

  public String getPrimary() {
    return primary;
  }

  public String getId() {
    return id;
  }

  public String getOwnername() {
    return ownername;
  }

  public String getOwner() {
    return owner;
  }

  public String getPer_page() {
    return per_page;
  }

  public int getTotal() {
    return total;
  }

  public int getPage() {
    return page;
  }
}