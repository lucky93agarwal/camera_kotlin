package com.example.mycamera

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.Camera
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    private lateinit var galleryOpenId: AppCompatButton
    private lateinit var cameraOpenId: AppCompatButton
    private lateinit var viewImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkCameraHardware(this)
        init()

    }

    private fun init() {
        galleryOpenId = findViewById(R.id.button)
        cameraOpenId = findViewById(R.id.buttonCamera)
        viewImage = findViewById(R.id.img)

        galleryOpenId.setOnClickListener {
            chooseImageGallery()
        }
        cameraOpenId.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startCamera.launch(cameraIntent)
        }
    }

    private var startCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                /* val data: Intent? = result.data*/
                Log.i("dataLucky", result.resultCode.toString())
                val photo = result.data!!.extras!!["data"] as Bitmap?
                // Set the image in imageview for display
                viewImage.setImageBitmap(photo)

            }
        }
    private var startOnGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                /* val data: Intent? = result.data*/
                // Set the image in imageview for display
                viewImage.setImageURI(result.data?.data)

            }
        }




    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startOnGallery.launch(intent)
    }

    private fun checkCameraHardware(context: Context): Boolean {
        if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            Log.i("checkPermission", "Result : this device has a camera")
            checkDeviceVersion()
            return true
        } else {
            // no camera on this device
            Log.i("checkPermission", "Result : no camera on this device")
            return false
        }
    }

    private fun checkDeviceVersion() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // only for gingerbread and newer versions
            Log.i("checkPermission", "Device Version Result : your device version is  33")
            onClickRequestPermission()
        } else {
            Log.i("checkPermission", "Device Version Result : less then device version is  33")
        }
    }

    private fun onClickRequestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {

            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA
                )
            }

            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA
                )
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("checkPermission: ", "Granted")
            } else {
                Log.i("checkPermission: ", "Denied")
            }
        }
}