package com.example.emojify

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.util.SparseArray
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.util.size
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.internal.GmsLogger
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector
import kotlinx.coroutines.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class MainModel(var context: Application) {

    val pictureIntent: LiveData<Intent>
        get() = _pictureIntent

    val filePath: LiveData<String>
        get() = _filePath

    val bitmap: LiveData<Bitmap>
        get() = _bitmap

    var _pictureIntent = MutableLiveData<Intent>()

    var _filePath = MutableLiveData<String>()

    var _bitmap = MutableLiveData<Bitmap>()

   var localFilePath : String? = null
    var detector: FaceDetector? = null

    var localbitmap: Bitmap? = null

    var faces: SparseArray<Face>? = null

    var emojiBitmap : Bitmap? = null

    var finalBitmap : Bitmap? = null

    private val SMILING_PROB_THRESHOLD = .15
    private val EYE_OPEN_PROB_THRESHOLD = .5

    fun launchCamera() {
        var intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (intent.resolveActivity(context.packageManager) != null) {
            var file: File = getTempFile()
            if (file != null) {
                var pictureUri =
                    FileProvider.getUriForFile(context, "com.example.android.fileprovider", file)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri)
                _pictureIntent.value = intent
            }
        }
    }

    private fun getTempFile(): File {
        var timestamp: String = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        var pictureFile = "emojify_" + timestamp
        var storageDir = context.getExternalCacheDir()
        var image = File.createTempFile(pictureFile, ".jpg", storageDir)
        _filePath.value = image.absolutePath
        localFilePath = image.absolutePath
        return image
    }


    fun buildDetector(bitmap: Bitmap){
        detector = FaceDetector.Builder(context)
            .setTrackingEnabled(false).setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
            .build()
        localbitmap = bitmap
        faces = detectFaces()        //faces = detectFaces()

        if (faces!!.size == 0) {
            Toast.makeText(context, "No face Detected", Toast.LENGTH_SHORT).show()
        }

        for (i in 0..faces!!.size() - 1) {
            when (calculateEmoji(faces!!.get(i))) {
                Emoji.SMILE -> emojiBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.smile)
                Emoji.FROWN -> emojiBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.frown)
                Emoji.LEFT_WINK_FROWN -> emojiBitmap= BitmapFactory.decodeResource(context.resources, R.drawable.leftwinkfrown)
                Emoji.RIGHT_WINK_FROWN -> emojiBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.rightwinkfrown)
                Emoji.LEFT_WINK -> emojiBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.leftwink)
                Emoji.RIGHT_WINK-> emojiBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.rightwink)
                Emoji.CLOSED_EYE_FROWN -> emojiBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.closed_frown)
                Emoji.CLOSED_EYE_SMILE -> emojiBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.closed_smile)
            }
            finalBitmap = addEmojiToFace(localbitmap, emojiBitmap, faces!!.get(i))
        }
        detector!!.release()
        _bitmap.value = finalBitmap
    }

    private fun addEmojiToFace(backgroundBitmap: Bitmap?, emojiBitmap: Bitmap?, face: Face?): Bitmap? {
        var emojiBitmap = emojiBitmap
        var resultBitmap : Bitmap = Bitmap.createBitmap(backgroundBitmap!!.width, backgroundBitmap.height, backgroundBitmap.config)
        var scaleFactor = 0.8F

        var newEmojiWidth = (face!!.width*scaleFactor).toInt()
        var newEmojiHeight: Int = (face!!.height*newEmojiWidth/emojiBitmap!!.width*scaleFactor).toInt()


        emojiBitmap = Bitmap.createScaledBitmap(emojiBitmap, newEmojiWidth, newEmojiHeight, false)
        val matrix = Matrix()
        matrix.postRotate(90F)

        emojiBitmap = Bitmap.createBitmap(emojiBitmap, 0, 0, emojiBitmap.width, emojiBitmap.height, matrix, true)
        val emojiPositionX = face.position.x + face.width / 2 - emojiBitmap.width / 2
        val emojiPositionY = face.position.y + face.height / 2 - emojiBitmap.height / 3

        val canvas = Canvas(resultBitmap)
        canvas.drawBitmap(backgroundBitmap, 0f, 0f, null)
        canvas.drawBitmap(emojiBitmap, emojiPositionX, emojiPositionY, null)
        return resultBitmap
    }


    fun detectFaces(): SparseArray<Face> {
        var frame = Frame.Builder().setBitmap(localbitmap).build()
        return detector!!.detect(frame)
    }


    private fun calculateEmoji(face: Face): Emoji {
        var isSmiling: Boolean = face.isSmilingProbability > SMILING_PROB_THRESHOLD
        var isRightEyeClose: Boolean = face.isRightEyeOpenProbability <= EYE_OPEN_PROB_THRESHOLD
        var isLeftEyeClose: Boolean = face.isLeftEyeOpenProbability <= EYE_OPEN_PROB_THRESHOLD
        var emoji: Emoji
        if (isSmiling) {
            if (!isLeftEyeClose && !isRightEyeClose) {
                emoji = Emoji.SMILE
            } else if (isRightEyeClose && isLeftEyeClose) {
                emoji = Emoji.CLOSED_EYE_SMILE
            } else if (isRightEyeClose && !isLeftEyeClose) {
                emoji = Emoji.RIGHT_WINK
            } else {
                emoji = Emoji.LEFT_WINK
            }
        } else {
            if (!isLeftEyeClose && !isRightEyeClose) {
                emoji = Emoji.FROWN
            } else if (isRightEyeClose && isLeftEyeClose) {
                emoji = Emoji.CLOSED_EYE_FROWN
            } else if (isRightEyeClose && !isLeftEyeClose) {
                emoji = Emoji.RIGHT_WINK_FROWN
            } else {
                emoji = Emoji.LEFT_WINK_FROWN
            }
        }
        Log.d("Emoji", "Face is ${emoji.name}")
        return emoji
    }

    private enum class Emoji {
        SMILE,
        FROWN,
        LEFT_WINK,
        RIGHT_WINK,
        LEFT_WINK_FROWN,
        RIGHT_WINK_FROWN,
        CLOSED_EYE_SMILE,
        CLOSED_EYE_FROWN
    }
}

