package fr.vpm.mypix.flickr.services;

import fr.vpm.mypix.flickr.beans.FlickrPhotos;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by vince on 24/03/18.
 */

public interface FlickrPhotosService {
  @GET("?method=flickr.people.getPublicPhotos&extras=url_s%2Curl_m%2Curl_o")
  Call<FlickrPhotos> listAlbums(@Query("user_id") String userId);
}
