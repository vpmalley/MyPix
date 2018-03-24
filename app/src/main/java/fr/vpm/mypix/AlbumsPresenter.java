package fr.vpm.mypix;

import android.content.Context;

import java.util.List;

import fr.vpm.mypix.album.Album;
import fr.vpm.mypix.local.LocalAlbumsRetriever;

class AlbumsPresenter {

  private final LocalAlbumsRetriever localAlbumsRetriever;

  public AlbumsPresenter() {
    localAlbumsRetriever = new LocalAlbumsRetriever();
  }

  void loadAlbums(Context context) {

    List<Album> albums = localAlbumsRetriever.getLocalAlbums(context);
    ((AlbumListActivity) context).onAlbumsLoaded(albums);
  }
}
