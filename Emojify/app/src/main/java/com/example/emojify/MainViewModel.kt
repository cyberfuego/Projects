package com.example.emojify

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

public class MainViewModel(var context: Application) : AndroidViewModel(context) {


    var _emojifyButtonClicked = MutableLiveData<Boolean>()

    var model: MainModel = MainModel(context)

    val emojifyButtonClicked: LiveData<Boolean> get() = _emojifyButtonClicked

    val pictureIntent : LiveData<Intent>
        get() = model.pictureIntent

    val bitmap : LiveData<Bitmap>
    get() = model.bitmap

    val filePath : LiveData<String>
    get() = model.filePath

    fun emojifyButtonClicked() {
        _emojifyButtonClicked.value = true
    }
    fun launchCamera(){
        model.launchCamera()
    }

    fun buildDetector(bitmap : Bitmap){
        model.buildDetector(bitmap)
    }
}