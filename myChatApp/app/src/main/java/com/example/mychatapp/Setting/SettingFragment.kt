package com.example.mychatapp.Setting

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.mychatapp.MainActivity
import com.example.mychatapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*

class SettingFragment: Fragment() {
    companion object {
        const val TAG = "SettingFragment:DEBUG:"
    }

    private lateinit var theUserProfile: ImageView

    private lateinit var imageView1:ImageView
    private lateinit var textView1:TextView

    private lateinit var imageView2:ImageView
    private lateinit var textView2:TextView

    private lateinit var imageView3:ImageView
    private lateinit var textView3:TextView

    private lateinit var userProfileImageView:ImageView
    private lateinit var userNameTextView:TextView
    private lateinit var userCreationTimeTextView:TextView
    private lateinit var arrowImageView:ImageView

    private lateinit var userEmail: String
    private lateinit var userPhone: String
    private var userPhotoUrl: String? = "n/a"

    // Firebase
    private lateinit var db: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String

    @SuppressLint("MissingInflatedId", "CutPasteId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        db = Firebase.database
//        db.useEmulator("10.0.2.2",9000)
        auth = Firebase.auth

        userEmail = "No user email."
        userPhone = "No user phone."

        if (auth.currentUser != null) {
            userId = auth.currentUser!!.uid
            userEmail = auth.currentUser!!.email.toString()
        }

        imageView1 = view.findViewById<ImageView>(R.id.imgCourseWebPage)
        textView1 = view.findViewById<TextView>(R.id.textCourseWebPage)
        imageView1.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse("https://www.sfu.ca/~xingdong/Teaching/CMPT362/web/cs65.html")
            startActivity(intent); }
        textView1.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse("https://www.sfu.ca/~xingdong/Teaching/CMPT362/web/cs65.html")
            startActivity(intent); }


        imageView2 = view.findViewById<ImageView>(R.id.imgSFUWebPage)
        textView2 = view.findViewById<TextView>(R.id.textSFUWebPage)
        imageView2.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse("http://www.sfu.ca/computing.html")
            startActivity(intent); }
        textView2.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse("http://www.sfu.ca/computing.html")
            startActivity(intent); }


        imageView3 = view.findViewById<ImageView>(R.id.imgGitHubWebPage)
        textView3 = view.findViewById<TextView>(R.id.textGitHubWebPage)
        imageView3.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse("https://github.com/Gravesky/cmpt362_final_project")
            startActivity(intent); }
        textView3.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse("https://github.com/Gravesky/cmpt362_final_project")
            startActivity(intent); }


        userProfileImageView = view.findViewById<ImageView>(R.id.imgMe)
        userNameTextView = view.findViewById<TextView>(R.id.text_view_Me_name)
        userCreationTimeTextView = view.findViewById<TextView>(R.id.text_view_user_since)
        arrowImageView = view.findViewById<ImageView>(R.id.imgMe_Arrow)

        userProfileImageView.setOnClickListener {toUserProfile(view)}
        userNameTextView.setOnClickListener {toUserProfile(view)}
        userCreationTimeTextView.setOnClickListener {toUserProfile(view)}
        arrowImageView.setOnClickListener {toUserProfile(view)}

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
//                Log.d(TAG, "onChildAdded:" + dataSnapshot.key!! + " >> ${dataSnapshot.value}")
                if (dataSnapshot.key!! == "userName") {
                    userNameTextView.text = dataSnapshot.value.toString()
                }
                if (dataSnapshot.key!! == "timestamp") {
                    val formatStr = "User since ${getDateTime(dataSnapshot.value as Long)}"
                    userCreationTimeTextView.text = formatStr
                }
                if (dataSnapshot.key!! == "userPhone") {
                    userPhone = dataSnapshot.value.toString()
                }
                if (dataSnapshot.key!! == "userEmail") {
                    userEmail = dataSnapshot.value.toString()
                }
                if (dataSnapshot.key!! == "photoUrl") {
                    userPhotoUrl = dataSnapshot.value.toString()
                    if (userPhotoUrl != "null") {
                        try {
                            loadImageIntoView(userProfileImageView,userPhotoUrl!!)
                        } catch (e:Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
//                Log.d(TAG, "onChildChanged: ${dataSnapshot.key} >> ${dataSnapshot.value}")
                if (dataSnapshot.key == "userName") {
                    userNameTextView.text = dataSnapshot.value.toString()
                }
                if (dataSnapshot.key == "timestamp") {
                    val formatStr = "User since ${getDateTime(dataSnapshot.value as Long)}"
                    userCreationTimeTextView.text = formatStr
                }
                if (dataSnapshot.key == "userPhone") {
                    userPhone = dataSnapshot.value.toString()
                }
                if (dataSnapshot.key == "userEmail") {
                    userEmail = dataSnapshot.value.toString()
                }
                if (dataSnapshot.key == "photoUrl") {
                    userPhotoUrl = dataSnapshot.value.toString()
                    if (userPhotoUrl != "null") {
                        loadImageIntoView(userProfileImageView,userPhotoUrl!!)
                    }
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.key!!)
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.key!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException())
            }
        }
        db.reference.child(MainActivity.USER_CHILD).child(userId).addChildEventListener(childEventListener)

        return view
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"onResume called")
    }

    private fun toUserProfile(view: View){
        val jumpToShow:Intent = Intent(context, UserProfileActivity::class.java)
        jumpToShow.putExtra("userName",userNameTextView.text)
        jumpToShow.putExtra("userEmail",userEmail)
        jumpToShow.putExtra("userPhone",userPhone)
        jumpToShow.putExtra("userPhoto",userPhotoUrl)
        startActivity(jumpToShow)
    }

    //ref: https://stackoverflow.com/questions/47250263/kotlin-convert-timestamp-to-datetime
    private fun getDateTime(s:Long): String? {
        return try {
            val sdf = SimpleDateFormat("yyyy/MM/dd")
            val netDate = Date(s)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.printStackTrace()
            "n/a"
        }
    }

    private fun loadImageIntoView(view: ImageView, url: String) {
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