package fr.vpm.mypix.flickr.services;

import fr.vpm.mypix.flickr.beans.FlickrPhotoset;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by vince on 24/03/18.
 */

public interface FlickrPhotosetService {
  @GET("?method=flickr.photosets.getPhotos&extras=url_m%2Curl_o&media=photos")
  Call<FlickrPhotoset> getAlbum(@Query("user_id") String userId, @Query("photoset_id") String photosetId);
}
