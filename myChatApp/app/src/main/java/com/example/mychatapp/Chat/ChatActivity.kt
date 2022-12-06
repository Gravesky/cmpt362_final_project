package com.example.mychatapp.Chat

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mychatapp.MainActivity
import com.example.mychatapp.R
import com.example.mychatapp.Util
import com.example.mychatapp.data.Group
import com.example.mychatapp.data.SingleMsg
import com.example.mychatapp.data.SingleMsgAdapter
import com.example.mychatapp.tool.MyButtonObserver
import com.example.mychatapp.tool.MyOpenDocumentContract
import com.example.mychatapp.tool.MyScrollToBottomObserver
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class ChatActivity : AppCompatActivity() {
    companion object {
        const val TAG = "ChatActivity:DEBUG:"
        const val NEW_CHAT_ACTION = "new_chat"
        const val EXIST_CHAT_ACTION = "exist_chat"
        const val TARGET_UID_KEY = "target_uid"
        var  groupId = "n/a"
    }

    private lateinit var db: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

//    private lateinit var groupId: String // initialized in initialNewGroup
    private lateinit var adapter: SingleMsgAdapter
    private lateinit var manager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //initial auth
        auth = Firebase.auth
        //initial db
        db = Firebase.database
//        db.useEmulator("10.0.2.2",9000)

        // View loading will be blocked until a groupId is assigned.
        if (intent.action == EXIST_CHAT_ACTION){
            // Started by a existing chat group (ChatHistoryFragment)
            groupId = intent.getStringExtra(ChatHistoryFragment.KEY_GROUP_ID).toString()
            loadView()
        }
        else {
            // Started by brand new chat group (ContactActivity)
            checkUserHasGroup(intent.getStringExtra(TARGET_UID_KEY).toString(), auth.currentUser!!.uid)
        }

    }

    override fun onPause() {
        adapter.stopListening()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        try {
            adapter.startListening()
        }catch (e: java.lang.Exception){
            e.printStackTrace()
        }
    }

    private fun checkUserHasGroup(targetUid: String, curUid: String){
        db.reference.child(MainActivity.USER_CHILD).child(curUid).child("friends")
            .child(targetUid).child("groupId").get().addOnSuccessListener {
            Log.d(TAG,"this target User has groupId = ${it.value}")
                groupId = if (it.value == "n/a") {
                    initialNewGroup()
                } else {
                    it.value.toString()
                }
                //Note. view must be loaded after the groupId is initialized
               loadView()
        }
    }

    private fun loadView(){
        Log.d(TAG,"Loading view!")
        val messageRef = db.reference.child(MainActivity.MESSAGES_CHILD).child(groupId)
        val options = FirebaseRecyclerOptions.Builder<SingleMsg>()
            .setQuery(messageRef, SingleMsg::class.java)
            .build()

        val myProgressBar: ProgressBar = findViewById(R.id.progressBar)
        val myRecyclerView: RecyclerView = findViewById(R.id.messageRecyclerView)
        val sendButton: ImageView = findViewById(R.id.sendButton)
        val sendImageButton: ImageView = findViewById(R.id.addMessageImageView)
        val userInputEditText:EditText = findViewById(R.id.messageEditText)

        adapter = SingleMsgAdapter(options, auth.currentUser!!.uid)
        myProgressBar.visibility = ProgressBar.INVISIBLE
        manager = LinearLayoutManager(this)
        manager.stackFromEnd = true

        myRecyclerView.layoutManager = manager
        // ref:https://stackoverflow.com/questions/35653439/recycler-view-inconsistency-detected-invalid-view-holder-adapter-positionviewh
        myRecyclerView.itemAnimator = null // mama mia!!!
        myRecyclerView.adapter = adapter

        // Scroll down when a new message arrives
        adapter.registerAdapterDataObserver(
            MyScrollToBottomObserver(myRecyclerView,adapter,manager)
        )

        // Disable the send button when there's no text in the input field
        userInputEditText.addTextChangedListener(MyButtonObserver(sendButton))

        sendButton.setOnClickListener{
            sendNewMessage(userInputEditText.text.toString())
            userInputEditText.text.clear()
        }

        sendImageButton.setOnClickListener{
            Log.d(TAG,"send image button clicked...")
            openDocument.launch(arrayOf("image/*"))
        }

        adapter.startListening()
    }

    // TODO: Convert to thread
    private fun sendNewMessage(text:String){
        if(text.isNotEmpty()){ // MyButtonObserver does not work as intended, so here is the fix.
            val thisUid = Firebase.auth.currentUser!!.uid
            val thisName = Firebase.auth.currentUser!!.displayName
            val thisUserPhotoUri: Uri? = Firebase.auth.currentUser!!.photoUrl
            val newMessage = SingleMsg(
                groupId,
                text,
                thisUid,
                thisName,
                ServerValue.TIMESTAMP,
                thisUserPhotoUri?.toString(),
                null,
            )
            val sendMsgThread = Thread{
                sendTask(newMessage, thisUid, thisName!!, text, groupId)
            }
            sendMsgThread.start()
        }
    }

    private fun sendTask(theMessage: SingleMsg, userId: String, userName:String, text:String, thisGroupId:String){
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.d(TAG,"sendMessage:Running on Main Thread")
        } else {
            Log.d(TAG,"sendMessage:Running on Background Thread")
        }
        //Post the message to the DB under the current group id.
        db.reference.child(MainActivity.MESSAGES_CHILD).child(thisGroupId).push().setValue(theMessage).addOnSuccessListener {
            Log.d(TAG,"message: $text sent")
        }
        //Update latest message of the Group
        val messageHash = HashMap<String, Any>()
        messageHash["speakerId"] = userId
        messageHash["userName"] = userName
        messageHash["message"] = text
        messageHash["timestamp"] = ServerValue.TIMESTAMP
        db.reference.child(MainActivity.GROUP_CHILD).child(thisGroupId).child("latestMSG").setValue(messageHash).addOnSuccessListener {
            Log.d(TAG, "message: $text has been updated to $thisGroupId group.")
        }
    }

    private fun initialNewGroup(): String{
        //Gather materials
        val targetUid = intent.getStringExtra(TARGET_UID_KEY) as String
        val thisUid = Firebase.auth.currentUser!!.uid
        //prepare new member list
        val memberList = ArrayList<String>()
        memberList.add(thisUid)
        memberList.add(targetUid)

        // Get the unique id generated by the DB to be the group id
        val newGroupId = db.reference.child(MainActivity.GROUP_CHILD).push().key.toString()
        // Initial the group entry
        val newGroup = Group(
            newGroupId,memberList,null,ServerValue.TIMESTAMP
        )
        // Create the new group on the db
        db.reference.child(MainActivity.GROUP_CHILD).child(newGroupId).setValue(newGroup).addOnSuccessListener {
            Toast.makeText(this,"Group is created with Gid = $newGroupId",Toast.LENGTH_LONG).show()
        }
        // Update the group list on currentUser
        updateGroupsOnUser(thisUid, targetUid, newGroupId)
        // Update the group list on targetUser
        updateGroupsOnUser(targetUid, thisUid, newGroupId)
        return newGroupId
    }

    private fun updateGroupsOnUser(userId:String, targetUid: String, newGroupId: String) {
        db.reference.child(MainActivity.USER_CHILD).child(userId).child("groups").get().addOnSuccessListener {
            Log.d(TAG,"Got current user groups data = $it")
            var groupList = ArrayList<String>()
            if (it.value != null) {
                groupList = it.value as ArrayList<String>
            }
            groupList.add(newGroupId) // add the new group
            db.reference.child(MainActivity.USER_CHILD).child(userId).child("groups").setValue(groupList).addOnSuccessListener {
                Toast.makeText(this,"user $userId groups is updated ",Toast.LENGTH_LONG).show()
            }

        }

        db.reference.child(MainActivity.USER_CHILD).child(userId).child("friends").child(targetUid).child("groupId").setValue(newGroupId).addOnSuccessListener {
            Log.d(TAG,"friends - group has been updated for user $userId")
        }
    }

    private val openDocument = registerForActivityResult(MyOpenDocumentContract()) { uri ->
        uri?.let {
            val user = auth.currentUser
            val userName:String = user!!.displayName.toString()
            val userId:String = user.uid
            val userPhotoUrl:String = user.photoUrl.toString()
            val uploadImageThread = Thread{
                onImageSelected(it, userName, userId, userPhotoUrl, groupId)
            }
            uploadImageThread.start()
        }
    }

    private fun onImageSelected(uri: Uri, userName:String, userId: String, userPhotoUrl:String, thisGroupId: String) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.d(TAG,"sendPicture:Running on Main Thread")
        } else {
            Log.d(TAG,"sendPicture:Running on Background Thread")
        }
        Log.d(TAG, "Uri: $uri")
        val tempMessage = SingleMsg(thisGroupId, null, userId, userName, ServerValue.TIMESTAMP, userPhotoUrl, Util.LOADING_IMAGE_URL)
        db.reference
            .child(MainActivity.MESSAGES_CHILD)
            .child(thisGroupId)
            .push()
            .setValue(
                tempMessage,
                DatabaseReference.CompletionListener { databaseError, databaseReference ->
                    if (databaseError != null) {
                        Log.w(
                            TAG, "Unable to write message to database.",
                            databaseError.toException()
                        )
                        return@CompletionListener
                    }
                    //Update latest message of the Group
                    val messageHash = HashMap<String, Any>()
                    messageHash["speakerId"] = userId
                    messageHash["userName"] = userName
                    messageHash["message"] = "[Image]"
                    messageHash["timestamp"] = ServerValue.TIMESTAMP
                    db.reference.child(MainActivity.GROUP_CHILD).child(thisGroupId).child("latestMSG").setValue(messageHash).addOnSuccessListener {
                        Log.d(TAG, "message: [Image] has been updated to $thisGroupId group.")
                    }
                    Log.d(TAG,"Building storage reference.")
                    // Build a StorageReference and then upload the file
                    val key = databaseReference.key
                    val storageReference = Firebase.storage
                        .getReference(userId)
                        .child("message")
                        .child(key!!)
                        .child(uri.lastPathSegment!!.toString())
                    putImageInStorage(storageReference, uri, key, userName, userId, userPhotoUrl, thisGroupId)
                })
    }

    private fun putImageInStorage(storageReference: StorageReference, uri: Uri, key: String?, userName:String, userId: String, userPhotoUrl:String, thisGroupId: String) {
        // First upload the image to Cloud Storage
        storageReference.putFile(uri)
            .addOnSuccessListener(
                this
            ) { taskSnapshot -> // After the image loads, get a public downloadUrl for the image
                // and add it to the message.
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        val mySingleMsg =
                            SingleMsg(thisGroupId, null, userId, userName, ServerValue.TIMESTAMP, userPhotoUrl, uri.toString())
                        db.reference
                            .child(MainActivity.MESSAGES_CHILD)
                            .child(thisGroupId)
                            .child(key!!)
                            .setValue(mySingleMsg)
                    }
            }
            .addOnFailureListener(this) { e ->
                Log.w(
                    TAG,
                    "Image upload task was unsuccessful.",
                    e
                )
            }
    }

    override fun onDestroy(){
        super.onDestroy()
        groupId = "n/a"
    }
}