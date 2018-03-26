package fr.vpm.mypix.flickr;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

import fr.vpm.mypix.AlbumsPresenter;
import fr.vpm.mypix.flickr.beans.FlickrPhotosets;
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
      albumsPresenter.onAlbumsRetrieved(new ArrayList<>());
    }

    @Override
    public void onFailure(@NonNull Call<FlickrPhotosets> call, @NonNull Throwable t) {
      Log.d(FlickrAlbumsRetriever.class.getSimpleName(), "Failed retrieving photosets. " + t.toString());
    }
  }
}
