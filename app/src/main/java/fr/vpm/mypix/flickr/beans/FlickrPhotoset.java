package fr.vpm.mypix.flickr.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlickrPhotoset {

  private String stat;
  private SinglePhotoset photoset;
  private String code;
  private String message;

  public String getStat() {
    return stat;
  }

  public SinglePhotoset getPhotoset() {
    return photoset;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}