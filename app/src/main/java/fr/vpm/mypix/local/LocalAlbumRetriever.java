package fr.vpm.mypix.local;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.Date;

import fr.vpm.mypix.AlbumFragment;
import fr.vpm.mypix.album.Album;
import fr.vpm.mypix.album.LocalPicture;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_ADDED;
import static android.provider.MediaStore.MediaColumns.DISPLAY_NAME;
import static android.provider.MediaStore.MediaColumns.TITLE;

public class LocalAlbumRetriever {

  @NonNull
  public void getLocalAlbum(Context context, AlbumFragment albumFragment, String albumId) {
    try (Cursor localPictures = context.getContentResolver().query(
        EXTERNAL_CONTENT_URI,
        new String[]{_ID, DISPLAY_NAME, BUCKET_ID, BUCKET_DISPLAY_NAME, DATA, TITLE, DATE_ADDED},
        BUCKET_ID + "= ?",
        new String[]{albumId},
        null
    )) {
      if (localPictures != null) {
        Album album = mapAlbum(localPictures);
        albumFragment.onAlbumRetrieved(album);
      }
    }
  }

  private Album mapAlbum(Cursor localPictures) {
    Album album = null;
    if (localPictures.moveToFirst()) {
      String bucketId = localPictures.getString(localPictures.getColumnIndex(BUCKET_ID));
      String bucketName = localPictures.getString(localPictures.getColumnIndex(BUCKET_DISPLAY_NAME));
      album = new Album(bucketId, bucketName, null, Album.Source.LOCAL, localPictures.getCount());
      while (localPictures.moveToNext()) {
        long pictureId = localPictures.getLong(localPictures.getColumnIndex(_ID));
        String fileName = localPictures.getString(localPictures.getColumnIndex(DATA));
        String displayName = localPictures.getString(localPictures.getColumnIndex(DISPLAY_NAME));
        String dateAdded = localPictures.getString(localPictures.getColumnIndex(DATE_ADDED));
        final LocalPicture localPicture = new LocalPicture(pictureId, fileName, displayName, new Date(Long.parseLong(dateAdded)));
        album.addPicture(localPicture);
      }
    }
    return album;
  }


}
