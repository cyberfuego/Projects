package com.example.emojify

import android.app.Application

class ApplicationClass : Application() {
     var mInstance : ApplicationClass ?= null

    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }

    fun getInstance() : ApplicationClass{
        if(mInstance == null){
            mInstance = this
        }
        return mInstance!!
    }
}