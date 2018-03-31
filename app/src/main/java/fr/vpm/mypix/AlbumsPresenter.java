package fr.vpm.mypix;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.vpm.mypix.album.Album;
import fr.vpm.mypix.flickr.FlickrAlbumsRetriever;
import fr.vpm.mypix.flickr.services.FlickrRetrofit;
import fr.vpm.mypix.local.LocalAlbumsRetriever;
import retrofit2.Retrofit;

public class AlbumsPresenter {

  private final LocalAlbumsRetriever localAlbumsRetriever;
  private final FlickrAlbumsRetriever flickrAlbumsRetriever;
  private Set<Album> allAlbums = new HashSet<>();
  private Context context;

  public AlbumsPresenter(final Context context) {
    localAlbumsRetriever = new LocalAlbumsRetriever();
    Retrofit flickrRetrofit = new FlickrRetrofit().getFlickrRetrofit(context);
    flickrAlbumsRetriever = new FlickrAlbumsRetriever(flickrRetrofit);
  }

  void loadAlbums(Context context) {
    this.context = context;

    allAlbums.clear();
    flickrAlbumsRetriever.getFlickrAlbums(this);
    List<Album> albums = localAlbumsRetriever.getLocalAlbums(context);
    allAlbums.addAll(albums);
    ((AlbumListActivity) this.context).onAlbumsLoaded(new ArrayList<>(allAlbums));
  }

  public void onAlbumsRetrieved(final List<Album> albums) {
    allAlbums.addAll(albums);
    ((AlbumListActivity) context).onAlbumsLoaded(new ArrayList<>(allAlbums));
  }
}
