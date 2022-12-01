package fr.vpm.mypix.flickr;

import androidx.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fr.vpm.mypix.AlbumsPresenter;
import fr.vpm.mypix.album.FlickrPicture;
import fr.vpm.mypix.flickr.beans.FlickrPhotos;
import fr.vpm.mypix.flickr.beans.Photo;
import fr.vpm.mypix.flickr.services.FlickrPhotosService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by vince on 24/03/18.
 */

public class FlickrPhotosRetriever {

  private final Retrofit flickrRetrofit;

  public FlickrPhotosRetriever(Retrofit flickrRetrofit) {
    this.flickrRetrofit = flickrRetrofit;
  }

  @NonNull
  private static List<FlickrPicture> mapPictures(List<Photo> photos) {
    List<FlickrPicture> flickrPictures = new ArrayList<>();
    for (Photo photo : photos) {
      FlickrPicture picture = new FlickrPicture(photo.getUrl_s(), photo.getUrl_m(), photo.getUrl_o(), photo.getTitle());
      flickrPictures.add(picture);
    }
    return flickrPictures;
  }

  public void getFlickrPictures(final AlbumsPresenter albumsPresenter) {
    FlickrPhotosService service = flickrRetrofit.create(FlickrPhotosService.class);
    service.listAlbums("107938954@N05").enqueue(new FlickrPhotosCallback(albumsPresenter));
  }

  private static class FlickrPhotosCallback implements Callback<FlickrPhotos> {

    private final AlbumsPresenter albumsPresenter;

    FlickrPhotosCallback(AlbumsPresenter albumsPresenter) {
      this.albumsPresenter = albumsPresenter;
    }

    @Override
    public void onResponse(Call<FlickrPhotos> call, Response<FlickrPhotos> response) {
      Log.d(FlickrPhotosRetriever.class.getSimpleName(), "retrieved photos");
      FlickrPhotos body = response.body();
      List<Photo> photos = body != null ? body.getPhotos().getPhoto() : new ArrayList<>();
      List<FlickrPicture> albums = mapPictures(photos);
    }

    @Override
    public void onFailure(Call<FlickrPhotos> call, Throwable t) {
      Log.d(FlickrPhotosRetriever.class.getSimpleName(), "Failed retrieving photos. " + t.toString());
    }
  }
}
