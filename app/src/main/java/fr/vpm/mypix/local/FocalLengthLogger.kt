package fr.vpm.mypix.local

import android.content.Context
import android.os.Build
import android.util.Log
import fr.vpm.mypix.album.Album
import fr.vpm.mypix.album.LocalPicture
import fr.vpm.mypix.album.Picture

class FocalLengthLogger {
    fun extractExifInfo(context: Context?, albums: List<Album>): List<ExifInfo> {
        var exifInfos = emptyList<ExifInfo>()
        val startTime = System.currentTimeMillis()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            exifInfos = albums.flatMap { album: Album ->
                album.pictures.mapNotNull { picture: Picture? ->
                    return@mapNotNull if (picture is LocalPicture) {
                        picture.extractExifInfo(context)
                    } else {
                        null
                    }
                }
            }
        }
        val endTime = System.currentTimeMillis()
        val computingDuration = endTime - startTime
        Log.d("ux-pic", "Took " + computingDuration + "ms to find EXIF data")
        return exifInfos
    }
}