package fr.vpm.mypix.flickr.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Photo {

  private int isfamily;
  private String width_o;
  private String height_o;
  private String title;
  private int farm;
  private int ispublic;
  private String url_s;
  private String server;
  private String url_o;
  private int isfriend;
  private String secret;
  private String height_s;
  private String owner;
  private String width_s;
  private String id;

  public int getIsfamily() {
    return isfamily;
  }

  public String getWidth_o() {
    return width_o;
  }

  public String getHeight_o() {
    return height_o;
  }

  public String getTitle() {
    return title;
  }

  public int getFarm() {
    return farm;
  }

  public int getIspublic() {
    return ispublic;
  }

  public String getUrl_s() {
    return url_s;
  }

  public String getServer() {
    return server;
  }

  public String getUrl_o() {
    return url_o;
  }

  public int getIsfriend() {
    return isfriend;
  }

  public String getSecret() {
    return secret;
  }

  public String getHeight_s() {
    return height_s;
  }

  public String getOwner() {
    return owner;
  }

  public String getWidth_s() {
    return width_s;
  }

  public String getId() {
    return id;
  }
}