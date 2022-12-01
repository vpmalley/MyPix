package fr.vpm.mypix.album;

import androidx.annotation.NonNull;

public interface Picture {

  @NonNull
  String getExtension();

  String getFileName();
}
