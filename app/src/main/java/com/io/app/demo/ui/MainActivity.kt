package com.io.app.demo.ui

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.io.app.demo.R
import com.io.app.demo.adapter.ImageAdapter
import com.io.app.demo.application.MainApp
import com.io.app.demo.callback.ApiListener
import com.io.app.demo.callback.ViewItemClickHandler
import com.io.app.demo.dagger.factory.ViewModelProvideFactory
import com.io.app.demo.databinding.ActivityMainBinding
import java.io.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), ApiListener, ViewItemClickHandler {


    lateinit var mainViewModel: MainViewModel
    lateinit var restAdapter: ImageAdapter
    lateinit var binding: ActivityMainBinding
    private val REQUEST_WRITE_PERMISSION = 786

    private var list = arrayOf(
        "https://cdn.wallpapersafari.com/36/6/WCkZue.png",
        "https://www.iliketowastemytime.com/sites/default/files/hamburg-germany-nicolas-kamp-hd-wallpaper_0.jpg",
        "https://images.hdqwalls.com/download/drift-transformers-5-the-last-knight-qu-5120x2880.jpg",
        "https://survarium.com/sites/default/files/calendars/survarium-wallpaper-calendar-february-2016-en-2560x1440.png"
    )

    private var uriList: ArrayList<Uri> = ArrayList()


    @Inject
    lateinit var viewModelFactory: ViewModelProvideFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainApp.appComponent.inject(this@MainActivity)
        mainViewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
    }

    private fun init() {
        binding.viewModel = mainViewModel
        binding.viewHandler = this

        restAdapter = ImageAdapter(this)
        binding.adapter = restAdapter


        registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        vararg permissions: String,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            downloadImage()
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                REQUEST_WRITE_PERMISSION
            )
        } else {
            downloadImage()
        }
    }

    private fun downloadImage() {
        if (mainViewModel.observer.isAsync) {
            mainViewModel.observer.isDownloadingStarted = true
            downloadImageAsync()
        }else msg("Please switch sync")
    }

    private var onComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context?, intent: Intent?) {
            Log.e("TAG", "receiver")
            val action = intent!!.action
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
                val downloadId = intent.getLongExtra(
                    DownloadManager.EXTRA_DOWNLOAD_ID, 0
                )
                Log.e(
                    "TAG",
                    " uri after download ${
                        (getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).getUriForDownloadedFile(
                            downloadId
                        )
                    }"
                )
                uriList.add(
                    (getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).getUriForDownloadedFile(
                        downloadId
                    )
                )
                restAdapter.mlist = uriList

                if (uriList.size >= 4) {
                    mainViewModel.observer.isDownloadingStarted = false
                }

            }

        }
    }

    private fun downloadImageAsync(
    ) {
        for (position in list.indices) {

            val filename = UUID.randomUUID().toString()
            try {
                val dm =
                    getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val downloadUri = Uri.parse(list[position])
                val request = DownloadManager.Request(downloadUri)
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(filename)
                    .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_PICTURES,
                        File.separator.toString() + filename + ".jpg"
                    )
                val id: Long = dm.enqueue(request)
                Log.e("TAG", " id $id")

                val c: Cursor = dm.query(DownloadManager.Query().setFilterById(id))

                val sizeIndex = c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)

                val downloadedIndex =
                    c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)

                val status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))

                when (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        Log.e("TAG", " uri after download ${dm.getUriForDownloadedFile(id)}")

                        Log.e("TAG", "Position : $position , Completed")
                    }

                    DownloadManager.STATUS_PAUSED -> {
                        Log.e("TAG", " Download manager paused")
                    }

                    DownloadManager.STATUS_PENDING -> {
                        Log.e("TAG", " Download manager pengind")
                    }

                    DownloadManager.STATUS_RUNNING -> {
                        Log.e("TAG", " Download manager running")
                    }

                    DownloadManager.STATUS_FAILED -> {
                        Log.e("TAG", " Download manager failed")
                    }
                }
                val size = c.getInt(sizeIndex).toLong()
                val downloaded = c.getInt(downloadedIndex).toLong()
                var progress: Double = downloaded * 100.0 / size

                c.close()
                Log.e("TAG", "closing cursor")
            } catch (e: Exception) {
                Log.e("TAG", "Exception ${e.localizedMessage}")
            }
        }
    }

    override fun msg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun isProgressShowing(show: Boolean) {
        //binding.pBar.isVisible = show
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onItemClick(v: View) {
        when (v.id) {
            R.id.tv_async -> {
                if (mainViewModel.observer.isAsync) {
                    mainViewModel.observer.isAsync = false
                }
            }

            R.id.tv_sync -> {
                if (!mainViewModel.observer.isAsync) {
                    mainViewModel.observer.isAsync = true
                }
            }

            R.id.tv_download -> {
                if (!mainViewModel.observer.isDownloadingStarted) {
                    uriList.clear()
                    restAdapter.clear()
                    requestPermission()
                }
            }
        }
    }

}