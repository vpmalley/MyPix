package fr.vpm.mypix;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fr.vpm.mypix.album.Album;
import fr.vpm.mypix.album.Picture;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_ADDED;
import static android.provider.MediaStore.MediaColumns.DISPLAY_NAME;
import static android.provider.MediaStore.MediaColumns.TITLE;

/**
 * Created by vince on 25/02/18.
 */

class AlbumsPresenter {

  void loadAlbums(Context context) {

    final Map<String, Album> albums = new HashMap<>();
    try (Cursor localAlbums = context.getContentResolver().query(
        EXTERNAL_CONTENT_URI,
        new String[]{_ID, DISPLAY_NAME, BUCKET_ID, BUCKET_DISPLAY_NAME, DATA, TITLE, DATE_ADDED},
        null,
        new String[0],
        null
    )) {
      if (localAlbums != null) {
        Album currentAlbum = null;
        if (localAlbums.moveToFirst()) {
          while (localAlbums.moveToNext()) {
            String bucketId = localAlbums.getString(localAlbums.getColumnIndex(BUCKET_ID));
            String bucketName = localAlbums.getString(localAlbums.getColumnIndex(BUCKET_DISPLAY_NAME));
            if (currentAlbum == null || !albums.containsKey(bucketId)) {
              currentAlbum = new Album(bucketId, bucketName, null);
              albums.put(bucketId, currentAlbum);
            }
            long pictureId = localAlbums.getLong(localAlbums.getColumnIndex(_ID));
            String fileName = localAlbums.getString(localAlbums.getColumnIndex(DATA));
            String displayName = localAlbums.getString(localAlbums.getColumnIndex(DISPLAY_NAME));
            String dateAdded = localAlbums.getString(localAlbums.getColumnIndex(DATE_ADDED));
            final Picture picture = new Picture(pictureId, fileName, displayName, new Date(Long.parseLong(dateAdded)));
            albums.get(bucketId).addPicture(picture);
          }
        }
      }
    }
    ((AlbumListActivity) context).onAlbumsLoaded(new ArrayList<>(albums.values()));
  }
}
