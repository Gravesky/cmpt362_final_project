package com.example.mychatapp.Chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.os.Looper.*
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mychatapp.MainActivity
import com.example.mychatapp.R
import com.example.mychatapp.data.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddContact : AppCompatActivity() {
    companion object {
        const val TAG = "AddContactActivity:DEBUG:"
    }

    private lateinit var userInput: EditText
    private lateinit var currentUid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        if (Firebase.auth.currentUser != null) {
            currentUid = Firebase.auth.currentUser!!.uid
        }

        userInput = findViewById<EditText>(R.id.addContactUserInput)
        val addButton = findViewById<Button>(R.id.addButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)

        addButton.setOnClickListener(this::onClick)
        cancelButton.setOnClickListener(this::onClick)
        //Todo: read user input
    }

    private fun onClick(view: View) {
        when (view.id) {
            R.id.addButton -> {
                Log.d(TAG,"add clicked")
                addFriendOnDb()
            }
            R.id.cancelButton -> {
                backToMainActivity()
            }
        }
    }

    private fun addFriendOnDb(){
        val input: String = userInput.text.toString()
        //TODO: get target user id
        val db = Firebase.database
//        db.useEmulator("10.0.2.2", 9000)
        val userRef = db.reference.child(MainActivity.USER_CHILD)
        val curUserId:String = Firebase.auth.currentUser!!.uid
        val curUserEmail:String = Firebase.auth.currentUser!!.email.toString()
        val curUserName:String = Firebase.auth.currentUser!!.displayName.toString()
        if (input == curUserEmail){
            Toast.makeText(this,"WARNING: You are adding yourself as a friend.", Toast.LENGTH_LONG).show()
        }
        else {
            // Check if the user that we are searching exist
            userRef.orderByChild("userEmail").equalTo(input)
                .get()
                .addOnSuccessListener{ it ->
                    Log.d(TAG,"result = $it")
                    // When return data-snapshot value is null, it means we did not found the specified user in the db.
                    if (it.value != null) { // Otherwise, we continue with the information exchange between two user profile.
                        val subSnapshot = it.children.first()
                        val updateDbThread = Thread{
                            updateBothFriendList(userRef, it, curUserName, curUserEmail)
                        }
                        if (subSnapshot.children.first().key  == "friends") { // This friend has "friends"
                            Log.d(TAG,"Friend list found...")
                            val curFriendMap = subSnapshot.children.first().value as HashMap<String, Any>
                            val result = curFriendMap[curUserId]
                            if (result == null) { // Among all the "friends", this friend is not a friend with the current user
                                Log.d(TAG,"There are NOT friend. Adding friend.")
                                updateDbThread.start()
                            }
                        }
                        else { // This friend does not have any friend. Let's be his first friend.
                            updateDbThread.start()
                        }
                        backToMainActivity()
                    }
                    else {
                        Toast.makeText(this,"User not found! Please try again!", Toast.LENGTH_LONG).show()
                    }
                }
            Log.d(TAG,"add friend func ended")
        }
    }

    private fun updateBothFriendList(userRef: DatabaseReference, friendInfo: DataSnapshot, thisUserName:String, thisUserEmail:String){
        if (myLooper() == Looper.getMainLooper()) {
            Log.d(TAG,"Running on Main Thread")
        } else {
            Log.d(TAG,"Running on Background Thread")
        }
        var friendMap = HashMap<String, Any>() // create a empty friend map in case of initialization

        val userLevelSnapShot = friendInfo.children.first() // 'it' only have one children in this call

        // Get friend name from this snapshot
        val userInfoMap = userLevelSnapShot.value as HashMap<String, Any>
        val friendName:String = userInfoMap["userName"].toString()
        val friendEmail:String = userInfoMap["userEmail"].toString()

        Log.d(TAG,"1 - userLevelSnapShot ${userLevelSnapShot.key} : ${userLevelSnapShot.value}")
        if (userLevelSnapShot.children.first().key  == "friends"){ // If the friend list is not empty, we will copy down the list
            // Note. this is based on the key in the list has no alphabet smaller than "f"
            friendMap = userLevelSnapShot.children.first().value as HashMap<String, Any>
            Log.d(TAG,"1 - prev friends found...")
        }
        else {
            Log.d(TAG,"1 - no prev friends found!")
        }

        val targetId = userLevelSnapShot.key
        Log.d(TAG,"found uid $targetId for $friendName")

        //ChatSnapShot HashMap
        val chatSnapShotMap = HashMap<String, Any>()
        chatSnapShotMap["groupId"] = "n/a"
        chatSnapShotMap["userName"] = thisUserName
        chatSnapShotMap["userEmail"] = thisUserEmail

        friendMap[currentUid] = chatSnapShotMap
        val hashObject = HashMap<String, HashMap<String, Any>>()
        hashObject["friends"] = friendMap
        Log.d(TAG,"hash obj = $hashObject")
        userRef.child(targetId.toString()).updateChildren(hashObject as Map<String, Any>)

        //update current user friend list
        userRef.child(currentUid).get().addOnSuccessListener{ it2 ->
            Log.d(TAG,"result 2 = $it2")
            var friendMap2 = HashMap<String, Any>()
            if (it2.children.first().key == "friends") { // friends list is empty
                val oldFriendMap2 = it2.value as HashMap<String, Any>
                friendMap2 = oldFriendMap2["friends"] as HashMap<String, Any>
                Log.d(TAG,"2 - prev friends found...")
            }
            else{
                Log.d(TAG,"2 - no prev friends found!")
            }

            //ChatSnapShot HashMap
            val chatSnapShotMap2 = HashMap<String, Any>()
            chatSnapShotMap2["groupId"] = "n/a"
            chatSnapShotMap2["userName"] = friendName
            chatSnapShotMap2["userEmail"] = friendEmail

            friendMap2[targetId.toString()] = chatSnapShotMap2
            val hashObject2 = HashMap<String, HashMap<String, Any>>()
            hashObject2["friends"] = friendMap2
            Log.d(TAG,"hash obj 2 = $hashObject2")
            userRef.child(currentUid).updateChildren(hashObject2 as Map<String, Any>)
        }
    }

    private fun backToMainActivity() {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}
