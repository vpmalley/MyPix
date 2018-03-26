package fr.vpm.mypix.flickr.services;

import fr.vpm.mypix.flickr.beans.FlickrPhotosets;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by vince on 24/03/18.
 */

public interface FlickrPhotosetsService {
  @GET("services/rest/?method=flickr.photosets.getList&api_key=&format=json&nojsoncallback=1")
  Call<FlickrPhotosets> listAlbums(@Query("user_id") String userId);
}
