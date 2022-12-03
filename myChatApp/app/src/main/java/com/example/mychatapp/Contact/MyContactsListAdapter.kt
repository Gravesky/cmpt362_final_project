package com.example.mychatapp.Contact

import android.content.Context
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.mychatapp.MainActivity
import com.example.mychatapp.R
import com.example.mychatapp.Util
import com.example.mychatapp.data.User
import com.example.mychatapp.data.UserMessages_Database.UserMessages
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

//Firebase variables
private lateinit var db: FirebaseDatabase

class MyContactsListAdapter(private val context: Context, private var list: ArrayList<String>): BaseAdapter() {

    override fun getItem(position: Int): Any {
        return list.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val view = View.inflate(context, R.layout.adapter_contacts_list_layout,null)
        val imageViewPortrait = view.findViewById<ImageView>(R.id.contactProtrait)
        val textViewName = view.findViewById<TextView>(R.id.contactName)

        ///////////////////////////////////////////////////////////////
        // TODO:
        //      目前无法显示图像，不知道照片的位置和存储的链接的类型
        //var path = list.get(position).profile_pic
        //val file=File(path)
        //val imgUri = Uri.fromFile(file)
        //val bitmap = Util.getBitmap(context, imgUri)
        //imageViewPortrait.setImageBitmap(bitmap)
        imageViewPortrait.setImageResource(R.drawable.sfu_logo)
        ///////////////////////////////////////////////////////////////

        //Initialize database variable
        db = Firebase.database
        db.useEmulator("10.0.2.2", 9000)

        //Get the friend's information
        val friendRef = db.reference.child(MainActivity.USER_CHILD).child(list[position])

        val userListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val friend = snapshot.getValue<User>()

                textViewName.text = friend?.userName

//                //TODO: get the image and set the profile pic!!!
//                val link = friend?.photoUrl
//                if(link?.startsWith("gs://")!!){
//                    val storageReference = Firebase.storage.getReferenceFromUrl(link)
//                    storageReference.downloadUrl.addOnSuccessListener {
//                        val downloadUrl = it.toString()
//                        Glide.with(view.context).load(downloadUrl).into(imageViewPortrait)
//                    }
//                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        }

        friendRef.addValueEventListener(userListener)





        return view
    }

    fun replace(newList:ArrayList<String>){
        list = newList
    }

//    fun getTheId(position: Int): String {
//        return list.get(position).acc_id
//    }
//    fun getTheName(position: Int): String {
//        return list.get(position).name
//    }
//    fun getTheEmail(position: Int): String {
//        return list.get(position).email
//    }

}