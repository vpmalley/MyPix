package fr.vpm.mypix.flickr.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlickrPhotosets {
  private String stat;
  private Photosets photosets;
  private String code;
  private String message;

  public String getStat() {
    return stat;
  }

  public Photosets getPhotosets() {
    return photosets;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}