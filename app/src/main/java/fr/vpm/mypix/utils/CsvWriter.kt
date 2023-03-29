package fr.vpm.mypix.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import fr.vpm.mypix.local.ExifInfo
import java.io.File
import java.io.FileWriter

class CsvWriter {

    fun write(activity: Activity, exifInfo: List<ExifInfo>) {
        val folder = activity.getExternalFilesDir("export")
        val exifFile = File(folder, "exif-all")
        FileWriter(exifFile).use { writer ->

            val headers = listOf(
                "name",
                "extension",
                "make",
                "model",
                "focal length",
                "focal length (35mm)"
            ).joinToString(",", postfix = "\n")
            writer.append(headers)
            exifInfo.forEach { exif ->
                writer.append(exif.toCsvString())
            }
            writer.flush()
        }

//        val sendIntent = Intent()
//        sendIntent.action = Intent.ACTION_SEND
//        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(exifFile))
//        sendIntent.type = "text/csv"
//        activity.startActivity(Intent.createChooser(sendIntent, "SHARE"))
    }
}