package fr.vpm.mypix;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import fr.vpm.mypix.album.LocalDisplayPicture;
import fr.vpm.mypix.album.Picture;
import fr.vpm.mypix.album.PictureWithUri;

public class SharePicturesWithDialog {

  void sharePicture(View view, Picture picture) {
    if (picture instanceof LocalDisplayPicture) {
      AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
      builder.setTitle(R.string.pick_picture)
          .setItems(getPictureDescriptions((LocalDisplayPicture) picture), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              Picture pickedPicture = ((LocalDisplayPicture) picture).getPictures().get(which);
              sharePictureWithUri(view.getContext(), (PictureWithUri) pickedPicture);
            }
          });
      builder.create().show();
    } else if (picture instanceof PictureWithUri) {
      sharePictureWithUri(view.getContext(), (PictureWithUri) picture);
    }
  }

  @NonNull
  private String[] getPictureDescriptions(LocalDisplayPicture picture) {
    String[] picturesDescriptions = new String[picture.getPictures().size()];
    for (int i = 0; i < picture.getPictures().size(); i++) {
      Picture pictureToShare = picture.getPictures().get(i);
      if (pictureToShare instanceof PictureWithUri) {
        picturesDescriptions[i] = ((PictureWithUri) pictureToShare).getFileNameWithExtension();
      }
    }
    return picturesDescriptions;
  }

  private void sharePictureWithUri(Context context, PictureWithUri picture) {
    Intent shareIntent = new Intent();
    shareIntent.setAction(Intent.ACTION_SEND);
    shareIntent.putExtra(Intent.EXTRA_STREAM, picture.getUri());
    shareIntent.setType("image/*");
    context.startActivity(Intent.createChooser(shareIntent, picture.getFileNameWithExtension()));
  }

}
