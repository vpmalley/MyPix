package fr.vpm.mypix.album;

import java.util.Date;

/**
 * Created by vince on 26/02/18.
 */

public class Picture {
  private final String path;
  private final String displayName;
  private final Date additionDate;

  public Picture(String path, String displayName, Date additionDate) {
    this.path = path;
    this.displayName = displayName;
    this.additionDate = additionDate;
  }

  public String getPath() {
    return path;
  }

  public String getDisplayName() {
    return displayName;
  }

  public Date getAdditionDate() {
    return additionDate;
  }
}
