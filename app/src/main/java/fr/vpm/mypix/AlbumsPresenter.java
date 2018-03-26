package fr.vpm.mypix;

import android.content.Context;

import java.util.List;

import fr.vpm.mypix.album.Album;
import fr.vpm.mypix.flickr.FlickrAlbumsRetriever;
import fr.vpm.mypix.local.LocalAlbumsRetriever;

public class AlbumsPresenter {

  private final LocalAlbumsRetriever localAlbumsRetriever;
  private final FlickrAlbumsRetriever flickrAlbumsRetriever;
  private Context context;

  public AlbumsPresenter() {
    localAlbumsRetriever = new LocalAlbumsRetriever();
    flickrAlbumsRetriever = new FlickrAlbumsRetriever();
  }

  void loadAlbums(Context context) {
    this.context = context;

    flickrAlbumsRetriever.getFlickrAlbums(this);
    List<Album> albums = localAlbumsRetriever.getLocalAlbums(context);
    ((AlbumListActivity) this.context).onAlbumsLoaded(albums);
  }

  public void onAlbumsRetrieved(final List<Album> albums) {
    ((AlbumListActivity) context).onAlbumsLoaded(albums);
  }
}
