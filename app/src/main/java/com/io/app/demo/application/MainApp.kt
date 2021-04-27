package com.io.app.demo.application

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.SparseIntArray
import com.io.app.demo.dagger.component.ApplicationComponent
import com.io.app.demo.dagger.component.DaggerApplicationComponent

@Suppress("DEPRECATION")
class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        mInstance = this

        _initApp()
    }

    fun _initApp(){
        appComponent = DaggerApplicationComponent.builder().application(this)!!.build()
        appComponent.inject(mInstance)

    }

    companion object {
        lateinit var mInstance: MainApp
        lateinit var appComponent: ApplicationComponent

        val progressState = SparseIntArray()

        /**
         * Create Instance of Application
         */
        @Synchronized
        fun getInstance(): MainApp = mInstance

        @Synchronized
        fun component(): ApplicationComponent = appComponent


        fun isNetworkConnected(context: Context): Boolean {
            var connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val nw = connectivityManager.activeNetwork ?: return false
                val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
                return when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    //for other device how are able to connect with Ethernet
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    //for check internet over Bluetooth
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                    else -> false
                }
            } else {
                val nwInfo = connectivityManager.activeNetworkInfo ?: return false
                return nwInfo.isConnected
            }
        }

    }
}