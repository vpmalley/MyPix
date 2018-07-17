package fr.vpm.mypix;

import java.util.List;

import fr.vpm.mypix.album.Picture;

interface ActionModeManager {

  void startActionMode();

  void endActionMode();

  void onSelectedItemsChanged(final List<Picture> selectedPictures);
}
