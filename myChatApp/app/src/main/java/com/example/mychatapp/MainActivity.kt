package com.example.mychatapp

import android.content.Context
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.mychatapp.Chat.AddContact
import com.example.mychatapp.Chat.ChatHistoryFragment
import com.example.mychatapp.Contact.ContactsFragment
import com.example.mychatapp.Setting.SettingFragment
import com.example.mychatapp.auth.SignInActivity
import com.example.mychatapp.services.Notification
import com.firebase.ui.auth.AuthUI
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class MainActivity : AppCompatActivity() {

    companion object{
        const val TAG = "MainActivity:DEBUG:"
        const val MESSAGES_CHILD = "messages"
        const val GROUP_CHILD = "groups"
        const val USER_CHILD = "users"
        //Notification variable
        lateinit var myIntent : Intent
        var isSet = false
    }

    //Fragment Variables
    private lateinit var fragmentChatHistory: ChatHistoryFragment
    private lateinit var fragmentContects: ContactsFragment
    private lateinit var fragmentSetting: SettingFragment
    private lateinit var fragments:ArrayList<Fragment>
    private lateinit var myFragmentStateAdapter: FragmentStateAdapter
    private lateinit var viewPager: ViewPager2

    //Tab Variables
    private lateinit var tab: TabLayout
    private lateinit var tabLayoutMediator: TabLayoutMediator
    private lateinit var tabConfigurationStrategy: TabLayoutMediator.TabConfigurationStrategy
    private val TAB_TEXT = arrayOf("Chat", "Friends", "Me")
    private val TAB_ICON = listOf<Int>(R.drawable.ic_chat_70x70, R.drawable.ic_friends_70x70, R.drawable.ic_me_70x70)

    private lateinit var  fragmentTransaction: FragmentTransaction

    //////////////////////////////FireBase代碼//////////////////////////
    private lateinit var auth: FirebaseAuth
    // TODO: Message Adapter and Chat Adapter
//    private lateinit var adapter: FriendlyMessageAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // REMOVE OR DISABLE THIS
        /*if (BuildConfig.DEBUG) {
            Firebase.database.useEmulator("10.0.2.2", 9000)
            Firebase.auth.useEmulator("10.0.2.2", 9099)
            Firebase.storage.useEmulator("10.0.2.2", 9199)
        }*/
        // Off-line feature
        if(!isSet) { // Set persistence can only be called once!
            Log.d(TAG,"Calling database to set persistence !")
            Firebase.database.setPersistenceEnabled(true)
            isSet = true
        }

        // Initialize Firebase Auth and check if the user is signed in
        auth = Firebase.auth
        if (auth.currentUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            return
        }

        //Initialize fragments
        fragmentChatHistory = ChatHistoryFragment()
        fragmentContects = ContactsFragment()
        fragmentSetting = SettingFragment()

        fragments = ArrayList()
        fragments.add(fragmentChatHistory)
        fragments.add(fragmentContects)
        fragments.add(fragmentSetting)

        //Initialize tab variables and viewpager
        tab = findViewById(R.id.tab)
        viewPager = findViewById(R.id.viewpager)
        myFragmentStateAdapter = MyFragmentsStateAdapter(this, fragments)
        viewPager.adapter = myFragmentStateAdapter

        //Setting tab display and functions
        tabConfigurationStrategy =TabLayoutMediator.TabConfigurationStrategy(){
                tab:TabLayout.Tab, position:Int ->
            tab.text = TAB_TEXT[position]
            val imageId = TAB_ICON.get(position)
            val drawable = resources.getDrawable(imageId)
            tab.icon = drawable
        }
        tabLayoutMediator   = TabLayoutMediator(tab, viewPager, tabConfigurationStrategy)
        tabLayoutMediator.attach()

//        //////////////////////////////临时设置DataBasel里的一个人，UI测试代码/////////////！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
//        database =  UserMessagesDatabase.getInstance(this)
//        databaseDao = database.userMessagesDatabaseDao
//        repository = UserMessagesRepository(databaseDao)
//        factory = UserMessagesViewModelFactory(repository)
//        viewModel = ViewModelProvider(this, factory).get(UserMessagesViewModel::class.java)
//        //因为acc_id不能重复，所以在第一遍运行后需要注释掉后边的代码
//        //TODO:因为acc_id不能重复，所以在第一遍运行后需要注释掉后边的代码
//        val UserMessagesOne = UserMessages()
//        UserMessagesOne.acc_id = "XCNV_DEFSwdaSA_asefrsd"
//        UserMessagesOne.name = "Ni XX"
//        UserMessagesOne.email = "sfu666@sfu.ca"
//        UserMessagesOne.phone = "110120119"
//        UserMessagesOne.profile_pic = "res/drawable/sfu_logo.png"
//        viewModel.insert(UserMessagesOne)
//
//        val UserMessagesTwo = UserMessages()
//        UserMessagesTwo.acc_id = "XCNV_DEFaasdasXFSA_asefrsd"
//        UserMessagesTwo.name = "ikun"
//        UserMessagesTwo.email = "sfu666@sfu.ca"
//        UserMessagesTwo.phone = "110120119"
//        UserMessagesTwo.profile_pic = "res/drawable/sfu_logo.png"
//        viewModel.insert(UserMessagesTwo)
//
//        val UserMessagesThree = UserMessages()
//        UserMessagesThree.acc_id = "XCNV_D425XFSA_asefrsd"
//        UserMessagesThree.name = "WuErFan"
//        UserMessagesThree.email = "sfu666@sfu.ca"
//        UserMessagesThree.phone = "110120119"
//        UserMessagesThree.profile_pic = "res/drawable/sfu_logo.png"
//        viewModel.insert(UserMessagesThree)


        ////////////////////////////////////////////////////////////////////

        //Start Message Notificatin Service
        println("debug: about to start the service")
        if(auth.currentUser != null){
            println("debug: service started")
            myIntent = Intent(this, Notification::class.java)
            startService(myIntent)
        }

    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in.
        if (auth.currentUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            return
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(auth.currentUser != null){
            stopService(myIntent)
        }
    }

    // TODO: Add option menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sign_out_menu -> {
                signOut()
                true
            }
            R.id.addFriend -> {
                addFriend()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun signOut() {
        stopService(myIntent)
        AuthUI.getInstance().signOut(this)
        val signOutIntent = Intent(this, SignInActivity::class.java)
        signOutIntent.action = "SignOut"
        startActivity(signOutIntent)
        finish()
    }

    private fun addFriend() {
        startActivity(Intent(this, AddContact::class.java))
    }

}