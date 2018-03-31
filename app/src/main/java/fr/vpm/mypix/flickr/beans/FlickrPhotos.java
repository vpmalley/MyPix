package fr.vpm.mypix.flickr.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlickrPhotos {

  private Photos photos;
  private String stat;
  private String code;
  private String message;

  public Photos getPhotos() {
    return photos;
  }

  public String getStat() {
    return stat;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}