package com.example.emojify

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.FaceDetector
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.util.size
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.emojify.databinding.ActivityMainBinding
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.FaceDetector.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    lateinit var mbinding: ActivityMainBinding
    lateinit var mviewmodel: MainViewModel
    private val REQUEST_STORAGE_PERMISSION = 1
    private var mTempPhotoPath: String? = null
    private val FILE_PROVIDER_AUTHORITY = "com.example.android.fileprovider"
    private val REQUEST_IMAGE_CAPTURE = 1
    private var filePath: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mviewmodel = ViewModelProviders.of(this)
            .get(MainViewModel(ApplicationClass().getInstance())::class.java)
        mbinding.viewmodel = mviewmodel
        mbinding.setLifecycleOwner(this@MainActivity)
        mviewmodel.bitmap.observe(this, Observer { setImage(mviewmodel.bitmap.value) })
        mviewmodel.emojifyButtonClicked.observe(this, Observer { checkPermisssion() })
        mviewmodel.pictureIntent.observe(
            this,
            Observer { launchIntent(mviewmodel.pictureIntent.value) })
        mviewmodel.filePath.observe(this, Observer { filePath = mviewmodel.filePath.value })
    }

    private fun checkPermisssion() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //request Permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_STORAGE_PERMISSION
            )
        } else {
            mviewmodel.launchCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_STORAGE_PERMISSION -> {
                if (grantResults.size > 0 && grantResults.get(0) == PackageManager.PERMISSION_GRANTED) {
                    mviewmodel.launchCamera()
                } else {
                    Toast.makeText(this, "Request not granted", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun launchIntent(intent: Intent?) {
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            var bitmap = resamplePic(filePath)
            mviewmodel.buildDetector(bitmap)
        } else {
            Toast.makeText(this, "Cant get photo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setImage(bitmap: Bitmap?) {
        mbinding.saveButton.show()
        mbinding.clearButton.show()
        mbinding.shareButton.show()
        mbinding.titleTextView.visibility = View.GONE
        mbinding.emojifyButton.visibility = View.GONE
        mbinding.imageView.setImageBitmap(bitmap)
    }

    fun resamplePic(imagePath: String?): Bitmap {

        // Get device screen size information
        val metrics = DisplayMetrics()
        val manager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        manager.defaultDisplay.getMetrics(metrics)

        val targetH = metrics.heightPixels
        val targetW = metrics.widthPixels

        // Get the dimensions of the original bitmap
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imagePath, bmOptions)
        val photoW = bmOptions.outWidth
        val photoH = bmOptions.outHeight

        // Determine how much to scale down the image
        val scaleFactor = Math.min(photoW / targetW, photoH / targetH)

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor

        return BitmapFactory.decodeFile(imagePath)
    }


}
