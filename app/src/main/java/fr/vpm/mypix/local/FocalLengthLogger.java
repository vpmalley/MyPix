package fr.vpm.mypix.local;

import android.content.Context;
import android.util.Log;

import java.util.List;

import fr.vpm.mypix.album.Album;
import fr.vpm.mypix.album.LocalPicture;

public class FocalLengthLogger {

    public void logFocalLength(Context context, List<Album> albums) {
        long startTime = System.currentTimeMillis();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            albums.forEach(album ->
                    album.getPictures().forEach(picture -> {
                                if (picture instanceof LocalPicture) {
                                    ((LocalPicture) picture).logFocalLength(context);
                                }
                            }
                    ));
        }
        long endTime = System.currentTimeMillis();
        long computingDuration = endTime - startTime;
        Log.d("ux-pic", "Took " + computingDuration + "ms to find EXIF data");
    }
}
