package com.example.emojify

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class BitmapUtils {
    fun createTempImageFile(context : Context) : File {
        val timeStamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context.externalCacheDir

        return File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )
    }
}