package com.example.mychatapp.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.example.mychatapp.MainActivity
import com.example.mychatapp.R
import com.example.mychatapp.data.User
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.sql.Timestamp

class SignInActivity : AppCompatActivity() {
    companion object {
        const val TAG = "SignInActivity:DEBUG:"
    }

    // ActivityResultLauncher and its callback function
    private val signIn: ActivityResultLauncher<Intent> =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()){
            Log.d(TAG,"SignIn callback ...")
            if (it.resultCode == RESULT_OK) {
                Log.d(TAG, "Sign in successful!")
                db = Firebase.database
//                db.useEmulator("10.0.2.2", 9000)
                // TODO: send user information to realtime database
                pushNewUser()

                // TODO: go back to main activity
                goToMainActivity()
            } else {
                Toast.makeText(
                    this,
                    "There was an error signing in",
                    Toast.LENGTH_LONG).show()

                val response = it.idpResponse
                if (response == null) {
                    Log.w(TAG, "Sign in canceled")
                } else {
                    Log.w(TAG, "Sign in error", response.error)
                }
            }
        }

//    private fun testRead(){
//        db.reference.child("test").child("Something").get().addOnSuccessListener {
//            Log.i("firebase", "Got value ${it.value}")
//        }.addOnFailureListener{
//            Log.e("firebase", "Error getting data", it)
//        }
//    }

    private fun pushNewUser(){
        val userRef = db.reference.child(MainActivity.USER_CHILD)
        val user = Firebase.auth.currentUser
        if (user != null){

            val userId = user.uid

            userRef.child(userId).get().addOnSuccessListener {
                if (it.value == null) {
                    Log.d(TAG,"user does not exist")
                    val friends = HashMap<String, Any>() // store uid as both key and value
                    val groups = ArrayList<String>()// store uid as both key and value
                    val userInfo = User(userId, user.displayName, user.email, ServerValue.TIMESTAMP, friends , groups, user.photoUrl.toString())

                    user.uid.let { it1 -> userRef.child(it1).setValue(userInfo) }
                }
            }
        }
    }

    // Firebase instance variables
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"On create called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Initialize FirebaseAuth
        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"On Start called")
        // If there is no signed in user, launch FirebaseUI
        // Otherwise head to MainActivity
        if (Firebase.auth.currentUser == null || intent.action == "SignOut") {
            intent.action = ""
            // Sign in with FirebaseUI, see docs for more details:
            // https://firebase.google.com/docs/auth/android/firebaseui
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setLogo(R.mipmap.ic_launcher)
                .setAvailableProviders(listOf(
                    AuthUI.IdpConfig.EmailBuilder().build(),
                ))
                .build()
            signIn.launch(signInIntent)
            Log.d(TAG,"sign-in Intent launched")

        } else {
            Log.d(TAG,"User is still logged in.")
//            pushNewUser()
//            goToMainActivity()
        }
    }


    private fun goToMainActivity() {
        Log.d(TAG,"BACK to MAIN.")
        startActivity(Intent(this, MainActivity::class.java))
//        finish()
    }
}