package com.example.mychatapp.Contact

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mychatapp.MainActivity
import com.example.mychatapp.R
import com.example.mychatapp.data.User
import com.example.mychatapp.data.UserMessages_Database.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class ContactsFragment: Fragment() {

    //////////////////////////////临时设置DataBase，UI测试代码/////////////
    private lateinit var database: UserMessagesDatabase
    private lateinit var databaseDao: UserMessagesDatabaseDao
    private lateinit var repository: UserMessagesRepository
    private lateinit var viewModel: UserMessagesViewModel
    private lateinit var factory: UserMessagesViewModelFactory
    ////////////////////////////////////////////////////////////////////
    private lateinit var myListView: ListView
    private lateinit var myAdapter: MyContactsListAdapter
    private lateinit var arrayList: ArrayList<UserMessages>

    //Firebase variables
    private lateinit var db: FirebaseDatabase
    private lateinit var friends:HashMap<String, Any>
    private lateinit var friendList: ArrayList<String>


    private lateinit var view1: View

    companion object {
        const val TAG = "ContactsFragment:DEBUG:"
        const val KEY_USER_ID = "userId"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)

        db = Firebase.database
        db.useEmulator("10.0.2.2", 9000)
        val userRef = db.reference.child(MainActivity.USER_CHILD).child(Firebase.auth.currentUser?.uid.toString()).child("friends")

        myListView = view.findViewById(R.id.runHistoryListView)
        friendList = ArrayList()
        myAdapter = MyContactsListAdapter(requireActivity(),friendList)
        myListView.adapter = myAdapter

        //TODO: create a listener reading /users/$uid/friends in the realtime database
        val userListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                //TODO: Get the friends list of the current user
                friendList = ArrayList()
                for(friend in snapshot.children){
                    friendList.add(friend.key!!)
                }

                //TODO: use list adapter to generate the listview
                myAdapter.replace(friendList)
                myAdapter.notifyDataSetChanged()
                //myListView.adapter = myAdapter

            }

            override fun onCancelled(error: DatabaseError) {
            }

        }

        userRef.addValueEventListener(userListener)

        //TODO: Generate the listview
//
//
        
        myListView.setOnItemClickListener { adapterView, view, i, l ->
            val thisUserId = myAdapter.getItem(i) as String
            Log.d(TAG,"clicked uid = $thisUserId")
            val myContactIntent = Intent(this.requireContext(),ContactActivity::class.java)
            myContactIntent.putExtra(KEY_USER_ID,thisUserId)
            startActivity(myContactIntent)
        }

        return view
    }

//    override fun onResume() {
//        super.onResume()
//        myAdapter.notifyDataSetChanged()
//    }

}