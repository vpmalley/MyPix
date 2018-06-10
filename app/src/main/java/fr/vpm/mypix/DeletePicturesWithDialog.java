package fr.vpm.mypix;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.util.List;

import fr.vpm.mypix.album.Picture;
import fr.vpm.mypix.album.PictureWithUri;

public class DeletePicturesWithDialog {

  void deletePictures(View view, List<Picture> pictures, OnPicturesDeleted onPicturesDeleted) {
    new AlertDialog.Builder(view.getContext())
        .setTitle(R.string.delete_pictures_title)
        .setMessage(R.string.delete_pictures_message)
        .setCancelable(true)
        .setNegativeButton(R.string.all_cancel, null)
        .setPositiveButton(R.string.all_ok, (dialog, which) -> {
          deletePicturesForGood(view.getContext(), pictures);
          onPicturesDeleted.onPicturesDeleted();
          Snackbar.make(view, R.string.snackbar_pictures_deleted, Snackbar.LENGTH_SHORT).show();
        })
        .create().show();
  }

  private void deletePicturesForGood(Context context, List<Picture> pictures) {
    for (Picture picture : pictures) {
      if (picture instanceof PictureWithUri) {
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.delete(((PictureWithUri) picture).getUri(), null, null);
      }
    }

  }

  interface OnPicturesDeleted {
    void onPicturesDeleted();
  }
}
