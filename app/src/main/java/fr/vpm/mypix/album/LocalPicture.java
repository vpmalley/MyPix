package fr.vpm.mypix.album;

import android.content.ContentUris;
import android.content.Context;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by vince on 26/02/18.
 */

public class LocalPicture implements PictureWithUri {

    private final long id;
    private final String path;
    private final String displayName;
    private final Date additionDate;

    public LocalPicture(long id, String path, String displayName, Date additionDate) {
        this.id = id;
        this.path = path;
        this.displayName = displayName;
        this.additionDate = additionDate;
    }

    public long getId() {
        return id;
    }

    public Uri getUri() {
        return ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
    }

    @Override
    public String getFileNameWithExtension() {
        return getFileName() + "." + getExtension();
    }

    public String getPath() {
        return path;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Date getAdditionDate() {
        return additionDate;
    }

    @NonNull
    @Override
    public String getExtension() {
        int dotIndex = path.lastIndexOf('.');
        if (dotIndex > -1 && dotIndex + 1 < path.length()) {
            return path.substring(dotIndex + 1);
        } else {
            return "";
        }
    }

    @Override
    public String getFileName() {
        int slashIndex = path.lastIndexOf('/');
        if (slashIndex > -1 && slashIndex + 1 < path.length()) {
            int extensionLength = getExtension().isEmpty() ? 0 : getExtension().length() + 1;
            return path.substring(slashIndex + 1, path.length() - extensionLength);
        } else {
            return displayName;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void logFocalLength(Context context) {
        InputStream in = null;
        try {
            in = context.getContentResolver().openInputStream(getUri());
            ExifInterface exifInterface = new ExifInterface(in);
            String make = exifInterface.getAttribute(ExifInterface.TAG_MAKE);
            String model = exifInterface.getAttribute(ExifInterface.TAG_MODEL);
            String device = make + " " + model;
            double focalLength = exifInterface.getAttributeDouble(ExifInterface.TAG_FOCAL_LENGTH, 0.0);
            int focalLength35 = exifInterface.getAttributeInt(ExifInterface.TAG_FOCAL_LENGTH_IN_35MM_FILM, 0);
            Log.d("ux-pic", "Picture " + getDisplayName() + " taken with " + device + " has focal length " + focalLength + " / " + focalLength35);

        } catch (IOException e) {
            // Handle any errors
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
