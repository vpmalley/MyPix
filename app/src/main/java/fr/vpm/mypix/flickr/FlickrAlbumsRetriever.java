package fr.vpm.mypix.flickr;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fr.vpm.mypix.album.Album;
import fr.vpm.mypix.album.FlickrPicture;
import fr.vpm.mypix.flickr.beans.FlickrPhotosets;
import fr.vpm.mypix.flickr.beans.Photoset;
import fr.vpm.mypix.flickr.persistence.RealmFlickrAlbumPersister;
import fr.vpm.mypix.flickr.persistence.RealmFlickrAlbumRetriever;
import fr.vpm.mypix.flickr.services.FlickrPhotosetsService;
import fr.vpm.mypix.local.LocalAlbumsRetriever;
import fr.vpm.mypix.utils.Connection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by vince on 24/03/18.
 */

public class FlickrAlbumsRetriever {

    private final Retrofit flickrRetrofit;

    private final Connection connection;
    private RealmFlickrAlbumRetriever realmFlickrAlbumRetriever;

    public FlickrAlbumsRetriever(Retrofit flickrRetrofit, Context context) {
        this.flickrRetrofit = flickrRetrofit;
        this.connection = new Connection(context);
        this.realmFlickrAlbumRetriever = new RealmFlickrAlbumRetriever();
    }

    @NonNull
    private static ArrayList<Album> mapAlbums(List<Photoset> photosets) {
        ArrayList<Album> albums = new ArrayList<>();
        for (Photoset photoset : photosets) {
            Album album = new Album(photoset.getId(), photoset.getTitle().get_content(), photoset.getDescription().get_content(), Album.Source.FLICKR, photoset.getPhotos());
            album.addPicture(new FlickrPicture(photoset.getPrimary_photo_extras().getUrl_s(), photoset.getPrimary_photo_extras().getUrl_m(), photoset.getPrimary_photo_extras().getUrl_o(), photoset.getTitle().get_content()));
            albums.add(album);
        }
        return albums;
    }

    public void getFlickrAlbums(final LocalAlbumsRetriever.OnAlbumsRetrievedListener onAlbumsRetrievedListener) {
        List<Album> albums = realmFlickrAlbumRetriever.retrieveAllAlbums();
        if (albums != null && !albums.isEmpty()) {
            onAlbumsRetrievedListener.onAlbumsRetrieved(albums);
        }
    }

    public void forceGetFlickrAlbums(LocalAlbumsRetriever.OnAlbumsRetrievedListener onAlbumsRetrievedListener) {
        FlickrPhotosetsService service = flickrRetrofit.create(FlickrPhotosetsService.class);
        service.listAlbums("107938954@N05").enqueue(new FlickrPhotosetsCallback(onAlbumsRetrievedListener));
    }

    private static class FlickrPhotosetsCallback implements Callback<FlickrPhotosets> {

        private final LocalAlbumsRetriever.OnAlbumsRetrievedListener onAlbumsRetrievedListener;

        private final RealmFlickrAlbumPersister realmFlickrAlbumPersister;

        FlickrPhotosetsCallback(LocalAlbumsRetriever.OnAlbumsRetrievedListener onAlbumsRetrievedListener) {
            this.onAlbumsRetrievedListener = onAlbumsRetrievedListener;
            realmFlickrAlbumPersister = new RealmFlickrAlbumPersister();
        }

        @Override
        public void onResponse(@NonNull Call<FlickrPhotosets> call, @NonNull Response<FlickrPhotosets> response) {
            FlickrPhotosets body = response.body();
            List<Photoset> photosets = body != null && body.getPhotosets() != null ? body.getPhotosets().getPhotoset() : new ArrayList<>();
            String logMsg = "retrieved ";
            if (photosets != null) {
                logMsg += photosets.size() + " photosets";
            } else {
                logMsg += "0 photosets";
            }
            if (body != null) {
                logMsg += " with code " + body.getCode() + " and message " + body.getMessage();
            }
            Log.d(FlickrAlbumsRetriever.class.getSimpleName(), logMsg);
            ArrayList<Album> albums = mapAlbums(photosets);
            realmFlickrAlbumPersister.updateAlbumsWithCover(albums);
            onAlbumsRetrievedListener.onAlbumsRetrieved(albums);
        }

        @Override
        public void onFailure(@NonNull Call<FlickrPhotosets> call, @NonNull Throwable t) {
            Log.d(FlickrAlbumsRetriever.class.getSimpleName(), "Failed retrieving photosets. " + t.toString());
        }
    }
}
