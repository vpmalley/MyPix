package fr.vpm.mypix.flickr.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Photo {

  private int isfamily;
  private String width_o;
  private String width_m;
  private String width_s;
  private String height_o;
  private String height_m;
  private String height_s;
  private String title;
  private int farm;
  private int ispublic;
  private String url_s;
  private String url_m;
  private String url_o;
  private String server;
  private int isfriend;
  private String secret;
  private int isprimary;
  private String owner;
  private String id;

  public int getIsfamily() {
    return isfamily;
  }

  public String getWidth_o() {
    return width_o;
  }

  public String getWidth_m() {
    return width_m;
  }

  public String getWidth_s() {
    return width_s;
  }

  public String getHeight_o() {
    return height_o;
  }

  public String getHeight_m() {
    return height_m;
  }

  public String getHeight_s() {
    return height_s;
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

  public String getUrl_m() {
    return url_m;
  }

  public String getUrl_o() {
    return url_o;
  }

  public String getServer() {
    return server;
  }

  public int getIsfriend() {
    return isfriend;
  }

  public String getSecret() {
    return secret;
  }

  public int getIsprimary() {
    return isprimary;
  }

  public String getOwner() {
    return owner;
  }

  public String getId() {
    return id;
  }
}