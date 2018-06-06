package fr.vpm.mypix;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.io.File;
import java.util.List;

import fr.vpm.mypix.album.Picture;
import fr.vpm.mypix.album.PictureWithUri;

public class DeletePicturesWithDialog {

  void deletePictures(View view, List<Picture> pictures) {
    new AlertDialog.Builder(view.getContext())
        .setTitle(R.string.delete_pictures_title)
        .setMessage(R.string.delete_pictures_message)
        .setCancelable(true)
        .setNegativeButton(R.string.all_cancel, null)
        .setPositiveButton(R.string.all_ok, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            deletePicturesForGood(pictures);
            Snackbar.make(view, R.string.snackbar_pictures_deleted, Snackbar.LENGTH_SHORT).show();
          }
        })
        .create().show();
  }

  private void deletePicturesForGood(List<Picture> pictures) {
    for (Picture picture : pictures) {
      if (picture instanceof PictureWithUri) {
        File file = new File(((PictureWithUri) picture).getUri().getPath());
        if (file.exists()) {
          file.delete();
        }
      }
    }

  }
}
