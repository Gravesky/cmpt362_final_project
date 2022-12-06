package com.example.mychatapp.Setting

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mychatapp.BuildConfig
import com.example.mychatapp.MainActivity
import com.example.mychatapp.R
import com.example.mychatapp.tool.MyOpenDocumentContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class UserProfileActivity : AppCompatActivity() {
    companion object {
        const val TAG = "UserProfileActivity:DEBUG:"
        private const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
    }

    // Firebase
    private lateinit var db:FirebaseDatabase
    private lateinit var auth:FirebaseAuth

    private lateinit var userPhotoImageView: ImageView
    private var tempPhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

//        if (BuildConfig.DEBUG) {
//            Firebase.database.useEmulator("10.0.2.2", 9000)
//            Firebase.auth.useEmulator("10.0.2.2", 9099)
//            Firebase.storage.useEmulator("10.0.2.2", 9199)
//        }

        db = Firebase.database

        auth = Firebase.auth

        val userNameEditText = findViewById<EditText>(R.id.editTextName)
        val userEmailEditText = findViewById<EditText>(R.id.editTextEmail)
        val userPhoneEditText = findViewById<EditText>(R.id.editTextPhone)

        // Load user photo if there is one
        userPhotoImageView = findViewById(R.id.imageProfile)
        val photoUrl = intent.getStringExtra("userPhoto")
        if (photoUrl != "n/a" && photoUrl != null && photoUrl != "null") {
            Log.d(TAG,"set image to : $photoUrl")
            loadImageIntoView(userPhotoImageView,photoUrl)
        }

        userNameEditText.hint = intent.getStringExtra("userName")
        userEmailEditText.hint = intent.getStringExtra("userEmail")
        userPhoneEditText.hint = intent.getStringExtra("userPhone")

        val saveButton = findViewById<Button>(R.id.btnSave)
        val cancelButton = findViewById<Button>(R.id.btnCancel)
        val changePhotoButton = findViewById<Button>(R.id.btnChangePhoto)

        changePhotoButton.setOnClickListener {
            Log.d(TAG,"change image button clicked...")
            openDocument.launch(arrayOf("image/*"))
        }

        saveButton.setOnClickListener {
            val thisUserAuth = auth.currentUser
            val userId = auth.currentUser!!.uid
            val thisUserRef : DatabaseReference= db.reference.child(MainActivity.USER_CHILD).child(userId)
            if (userNameEditText.text.isNotEmpty()){
                Log.d(TAG,"user name not empty, sending update...")
                // Update database
                thisUserRef.child("userName").setValue(userNameEditText.text.toString()).addOnSuccessListener {
                    Log.d(TAG,"userName has been successfully updated to ${userNameEditText.text}")
                }
                // Update authentication
                val profileNameUpdate = userProfileChangeRequest {
                    displayName = userNameEditText.text.toString()
                }
                thisUserAuth!!.updateProfile(profileNameUpdate).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d(TAG, "AUTH: User profile name updated.")
                    }
                }
            }
            if (userEmailEditText.text.isNotEmpty()){
                Log.d(TAG,"user email not empty, sending update...")
                // Update database
                thisUserRef.child("userEmail").setValue(userEmailEditText.text.toString()).addOnSuccessListener {
                    Log.d(TAG,"userEmail has been successfully updated to ${userEmailEditText.text}")
                }
                // Update authentication
                thisUserAuth!!.updateEmail(userEmailEditText.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d(TAG, "AUTH: User email address updated.")
                    }
                }
            }
            if (userPhoneEditText.text.isNotEmpty()){
                Log.d(TAG,"user phone not empty, sending update...")
                thisUserRef.child("userPhone").setValue(userPhoneEditText.text.toString()).addOnSuccessListener {
                    Log.d(TAG,"userPhone has been successfully updated to ${userPhoneEditText.text}")
                }
                // No need to update auth for phone number, as we do not use phone number.
            }

            // save & upload the image
            if (tempPhotoUri != null) { // If there is selected image, save and upload to database.
                onImageSelected(tempPhotoUri!!)
            }
            // Go back to the previous page
            onBackPressed()
        }

        cancelButton.setOnClickListener {
            onBackPressed()
        }

    }

    private val openDocument = registerForActivityResult(MyOpenDocumentContract()) { uri ->
        Log.d(TAG,"openDocument: opened image...")
        uri?.let {
            loadImageIntoView(userPhotoImageView, uri.toString()) // load the image temporarily into
            tempPhotoUri = uri // store the photo uri temporarily into the variable
        }
    }

    private fun onImageSelected(uri: Uri) {
        Log.d(TAG, "Uri: $uri")
        val user = auth.currentUser
        val userId = user!!.uid
        Log.d(TAG,"getting reference for database")
        db.reference
            .child(MainActivity.USER_CHILD)
            .child(userId)
            .child("photoUrl")
            .setValue(
                LOADING_IMAGE_URL,
                DatabaseReference.CompletionListener { databaseError, databaseReference ->
                    if (databaseError != null) {
                        Log.d(
                            TAG, "Unable to write message to database.",
                            databaseError.toException()
                        )
                        return@CompletionListener
                    }

                    // Build a StorageReference and then upload the file
                    val storageReference = Firebase.storage
                        .getReference(userId)
                        .child("userInfo")
                        .child(uri.lastPathSegment!!.toString())
                    Log.d(TAG,"db update complete, putting image to storage.")
                    putImageInStorage(storageReference, uri, userId)
                })
    }

    private fun putImageInStorage(storageReference: StorageReference, uri: Uri, uid: String) {
        val uploadTask = storageReference.putFile(uri)
        uploadTask.continueWithTask {
            if (!it.isSuccessful) {
                it.exception?.let {
                    throw it
                }
            }
            storageReference.downloadUrl
        }.addOnCompleteListener {
            if (it.isSuccessful) {
                val downloadUri = it.result
                Log.d(TAG,"download url retrieved: $downloadUri")
                db.reference
                    .child(MainActivity.USER_CHILD)
                    .child(uid)
                    .child("photoUrl")
                    .setValue(downloadUri.toString()).addOnSuccessListener {
                        Log.d(TAG,"set the database user photo uri to $downloadUri")
                    }
                val profilePhotoUrlUpdate = userProfileChangeRequest {
                    photoUri = downloadUri
                }
                auth.currentUser!!.updateProfile(profilePhotoUrlUpdate).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d(TAG, "AUTH: User profile photo URI updated.")
                    }
                }
            } else {
                // Handle failures
                Log.d(TAG, "Image upload task was unsuccessful.")
            }
        }
    }

    private fun loadImageIntoView(view: ImageView, url: String) {
        if (url.startsWith("gs://")) {
            val storageReference = Firebase.storage.getReferenceFromUrl(url)
            storageReference.downloadUrl
                .addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    Glide.with(view.context)
                        .load(downloadUrl)
                        .into(view)
                }
                .addOnFailureListener { e ->
                    Log.w(
                        TAG,
                        "Getting download url was not successful.",
                        e
                    )
                }
        } else {
            Glide.with(view.context).load(url).into(view)
        }
    }
}