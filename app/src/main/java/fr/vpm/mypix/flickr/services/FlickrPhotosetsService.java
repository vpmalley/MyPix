package fr.vpm.mypix.flickr.services;

import fr.vpm.mypix.flickr.beans.FlickrPhotosets;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by vince on 24/03/18.
 */

public interface FlickrPhotosetsService {
  @GET("?method=flickr.photosets.getList&primary_photo_extras=url_s%2Curl_m%2Curl_o")
  Call<FlickrPhotosets> listAlbums(@Query("user_id") String userId);
}
