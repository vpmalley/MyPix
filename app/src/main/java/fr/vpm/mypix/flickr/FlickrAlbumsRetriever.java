package fr.vpm.mypix.flickr;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fr.vpm.mypix.AlbumsPresenter;
import fr.vpm.mypix.album.Album;
import fr.vpm.mypix.flickr.beans.FlickrPhotosets;
import fr.vpm.mypix.flickr.beans.Photoset;
import fr.vpm.mypix.flickr.services.FlickrPhotosetsService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by vince on 24/03/18.
 */

public class FlickrAlbumsRetriever {

  @NonNull
  private static ArrayList<Album> mapAlbums(List<Photoset> photosets) {
    ArrayList<Album> albums = new ArrayList<>();
    for (Photoset photoset : photosets) {
      Album album = new Album(photoset.getId(), photoset.getTitle().get_content(), photoset.getDescription().get_content());
      albums.add(album);
    }
    return albums;
  }

  public void getFlickrAlbums(final AlbumsPresenter albumsPresenter) {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("https://api.flickr.com/")
        .addConverterFactory(JacksonConverterFactory.create())
        .build();

    FlickrPhotosetsService service = retrofit.create(FlickrPhotosetsService.class);
    service.listAlbums("107938954@N05").enqueue(new FlickrPhotosetsCallback(albumsPresenter));
  }

  private static class FlickrPhotosetsCallback implements Callback<FlickrPhotosets> {

    private final AlbumsPresenter albumsPresenter;

    FlickrPhotosetsCallback(AlbumsPresenter albumsPresenter) {
      this.albumsPresenter = albumsPresenter;
    }

    @Override
    public void onResponse(@NonNull Call<FlickrPhotosets> call, @NonNull Response<FlickrPhotosets> response) {
      Log.d(FlickrAlbumsRetriever.class.getSimpleName(), "retrieved photosets");
      FlickrPhotosets body = response.body();
      List<Photoset> photosets = body != null ? body.getPhotosets().getPhotoset() : new ArrayList<>();
      ArrayList<Album> albums = mapAlbums(photosets);
      albumsPresenter.onAlbumsRetrieved(albums);
    }

    @Override
    public void onFailure(@NonNull Call<FlickrPhotosets> call, @NonNull Throwable t) {
      Log.d(FlickrAlbumsRetriever.class.getSimpleName(), "Failed retrieving photosets. " + t.toString());
    }
  }
}
