package com.io.app.demo.ui

import android.content.Context
import androidx.databinding.BaseObservable
import androidx.lifecycle.AndroidViewModel
import com.io.app.demo.application.MainApp
import javax.inject.Inject

class MainViewModel @Inject constructor(
    context: Context
) : AndroidViewModel(MainApp.getInstance()) {


    val observer: MainObserver = MainObserver()

    inner class MainObserver : BaseObservable() {
        var isAsync: Boolean = true
            set(value) {
                field = value
                notifyChange()
            }

        var isDownloadingStarted = false
            set(value) {
                field = value
                notifyChange()
            }
    }


}
