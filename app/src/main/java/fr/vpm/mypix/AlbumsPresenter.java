package fr.vpm.mypix;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.vpm.mypix.album.Album;
import fr.vpm.mypix.album.AlbumDisplay;
import fr.vpm.mypix.flickr.FlickrAlbumsRetriever;
import fr.vpm.mypix.flickr.services.FlickrRetrofit;
import fr.vpm.mypix.local.LocalAlbumsRetriever;
import retrofit2.Retrofit;

public class AlbumsPresenter {

  private final LocalAlbumsRetriever localAlbumsRetriever;
  private final FlickrAlbumsRetriever flickrAlbumsRetriever;
  private List<Album> allAlbums = new ArrayList<>();
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
    localAlbumsRetriever.getLocalAlbums(context, this);
  }

  public void onAlbumsRetrieved(final List<Album> albums) {
    allAlbums.addAll(albums);
    Map<String, List<Album>> albumsByName = mapAlbumsByName(allAlbums);
    List<AlbumDisplay> albumDisplays = mapAlbumsToDisplays(albumsByName);
    ((AlbumListActivity) context).onAlbumsLoaded(albumDisplays);
  }

  @NonNull
  private Map<String, List<Album>> mapAlbumsByName(List<Album> allAlbums) {
    Map<String, List<Album>> albumsByName = new HashMap<>();
    for (Album newAlbum : allAlbums) {
      List<Album> albumsWithThisName = albumsByName.get(newAlbum.getName());
      if (albumsWithThisName == null) {
        albumsWithThisName = new ArrayList<>();
      }
      albumsWithThisName.add(newAlbum);
      albumsByName.put(newAlbum.getName(), albumsWithThisName);
    }
    return albumsByName;
  }

  private List<AlbumDisplay> mapAlbumsToDisplays(Map<String, List<Album>> albumsByName) {
    List<AlbumDisplay> albumDisplays = new ArrayList<>();
    for (List<Album> albumsWithThisName : albumsByName.values()) {
      Album firstAlbum = albumsWithThisName.get(0);
      AlbumDisplay albumDisplay = new AlbumDisplay(firstAlbum.getName(), firstAlbum.getPictures().get(0), albumsWithThisName);
      albumDisplays.add(albumDisplay);
    }
    return albumDisplays;
  }

}
