package fr.vpm.mypix.album;

import android.support.annotation.NonNull;

public interface Picture {

  @NonNull
  String getExtension();

  String getFileName();
}
