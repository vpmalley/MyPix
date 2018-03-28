package fr.vpm.mypix.flickr.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Photoset {
  private String date_update;
  private int visibility_can_see_set;
  private Description description;
  private int videos;
  private Title title;
  private int farm;
  private int needs_interstitial;
  private String primary;
  private String server;
  private String date_create;
  private int photos;
  private String secret;
  private String count_comments;
  private String count_views;
  private int can_comment;
  private String id;

  public String getDate_update() {
    return date_update;
  }

  public int getVisibility_can_see_set() {
    return visibility_can_see_set;
  }

  public Description getDescription() {
    return description;
  }

  public int getVideos() {
    return videos;
  }

  public Title getTitle() {
    return title;
  }

  public int getFarm() {
    return farm;
  }

  public int getNeeds_interstitial() {
    return needs_interstitial;
  }

  public String getPrimary() {
    return primary;
  }

  public String getServer() {
    return server;
  }

  public String getDate_create() {
    return date_create;
  }

  public int getPhotos() {
    return photos;
  }

  public String getSecret() {
    return secret;
  }

  public String getCount_comments() {
    return count_comments;
  }

  public String getCount_views() {
    return count_views;
  }

  public int getCan_comment() {
    return can_comment;
  }

  public String getId() {
    return id;
  }
}