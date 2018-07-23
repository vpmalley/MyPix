package fr.vpm.mypix;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.View;

import fr.vpm.mypix.album.Picture;
import fr.vpm.mypix.album.PictureWithUri;

public class SharePicturesWithDialog {

  void sharePicture(View snackbarContainerView, Context context, Picture picture) {
    if (picture instanceof PictureWithUri) {
      sharePictureWithUri(context, (PictureWithUri) picture);
    } else {

      Snackbar.make(snackbarContainerView, R.string.snackbar_pictures_cant_be_shared, Snackbar.LENGTH_SHORT).show();
    }
  }

  private void sharePictureWithUri(Context context, PictureWithUri picture) {
    Intent shareIntent = new Intent();
    shareIntent.setAction(Intent.ACTION_SEND);
    shareIntent.putExtra(Intent.EXTRA_STREAM, picture.getUri());
    shareIntent.setType("image/*");
    context.startActivity(Intent.createChooser(shareIntent, picture.getFileNameWithExtension()));
  }

}
