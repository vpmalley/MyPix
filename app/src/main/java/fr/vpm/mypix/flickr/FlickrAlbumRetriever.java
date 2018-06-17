package fr.vpm.mypix.flickr;

import android.support.annotation.NonNull;
import android.util.Log;

import fr.vpm.mypix.AlbumFragment;
import fr.vpm.mypix.album.Album;
import fr.vpm.mypix.album.FlickrPicture;
import fr.vpm.mypix.flickr.beans.FlickrPhotoset;
import fr.vpm.mypix.flickr.beans.Photo;
import fr.vpm.mypix.flickr.beans.SinglePhotoset;
import fr.vpm.mypix.flickr.persistence.RealmFlickrAlbumPersister;
import fr.vpm.mypix.flickr.persistence.RealmFlickrAlbumRetriever;
import fr.vpm.mypix.flickr.services.FlickrPhotosetService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by vince on 24/03/18.
 */

public class FlickrAlbumRetriever {

  private final Retrofit flickrRetrofit;
  private RealmFlickrAlbumRetriever realmFlickrAlbumRetriever;

  public FlickrAlbumRetriever(Retrofit flickrRetrofit) {
    this.flickrRetrofit = flickrRetrofit;
    this.realmFlickrAlbumRetriever = new RealmFlickrAlbumRetriever();
  }

  @NonNull
  private static Album mapAlbum(SinglePhotoset photoset) {
    final Album album = new Album(photoset.getId(), photoset.getTitle(), photoset.getTitle(), Album.Source.FLICKR);
    for (Photo photo : photoset.getPhoto()) {
      album.addPicture(new FlickrPicture(photo.getUrl_m(), photo.getUrl_o(), photo.getTitle()));
    }
    return album;
  }

  public void getFlickrAlbum(final AlbumFragment albumFragment, final String flickrAlbumId) {
    Album album = realmFlickrAlbumRetriever.retrieveAlbum(flickrAlbumId);
    if (album != null && album.getPictures().size() > 1) {
      albumFragment.onAlbumRetrieved(album);
    } else {
      FlickrPhotosetService service = flickrRetrofit.create(FlickrPhotosetService.class);
      service.getAlbum("107938954@N05", flickrAlbumId).enqueue(new FlickrPhotosetCallback(albumFragment));
    }
  }

  private static class FlickrPhotosetCallback implements Callback<FlickrPhotoset> {

    private final AlbumFragment albumFragment;

    private final RealmFlickrAlbumPersister realmFlickrAlbumPersister;

    FlickrPhotosetCallback(AlbumFragment albumFragment) {
      this.albumFragment = albumFragment;
      realmFlickrAlbumPersister = new RealmFlickrAlbumPersister();
    }

    @Override
    public void onResponse(Call<FlickrPhotoset> call, Response<FlickrPhotoset> response) {
      Log.d(FlickrAlbumRetriever.class.getSimpleName(), "retrieved photoset");
      FlickrPhotoset body = response.body();
      SinglePhotoset photoset = body != null ? body.getPhotoset() : null;
      Album album = mapAlbum(photoset);
      realmFlickrAlbumPersister.update(album);
      albumFragment.onAlbumRetrieved(album);
    }

    @Override
    public void onFailure(Call<FlickrPhotoset> call, Throwable t) {
      Log.d(FlickrAlbumRetriever.class.getSimpleName(), "Failed retrieving photoset. " + t.toString());
    }
  }
}
