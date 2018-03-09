package fr.vpm.mypix;

import android.content.Context;

import java.util.ArrayList;

import fr.vpm.mypix.album.Album;

class AlbumsPresenter {

  private final LocalAlbumsRetriever localAlbumsRetriever;

  public AlbumsPresenter() {
    localAlbumsRetriever = new LocalAlbumsRetriever();
  }

  void loadAlbums(Context context) {

    ArrayList<Album> albums = localAlbumsRetriever.getLocalAlbums(context);
    ((AlbumListActivity) context).onAlbumsLoaded(albums);
  }
}
