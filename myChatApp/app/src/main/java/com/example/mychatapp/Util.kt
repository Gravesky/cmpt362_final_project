package com.example.mychatapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.mychatapp.Setting.UserProfileActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*

object Util {
    const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"

    fun getBitmap(context: Context, imgUri: Uri): Bitmap {
        var bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(imgUri))
        val matrix = Matrix()
        matrix.setRotate(0f)
        var ret = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        return ret
    }

    //ref: https://stackoverflow.com/questions/47250263/kotlin-convert-timestamp-to-datetime
    fun getDateTime(s:Long): String? {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val netDate = Date(s)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.printStackTrace()
            "n/a"
        }
    }

    fun loadImageIntoView(view: ImageView, url: String) {
        if (url.startsWith("gs://")) {
            val storageReference = Firebase.storage.getReferenceFromUrl(url)
            storageReference.downloadUrl
                .addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    Glide.with(view.context.applicationContext)
                        .load(downloadUrl)
                        .into(view)
                }
                .addOnFailureListener { e ->
                    Log.w(
                        UserProfileActivity.TAG,
                        "Getting download url was not successful.",
                        e
                    )
                }
        } else {
            Glide.with(view.context).load(url).into(view)
        }
    }
}