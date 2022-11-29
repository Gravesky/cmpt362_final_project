package com.example.mychatapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.mychatapp.Chat.ChatHistoryFragment
import com.example.mychatapp.Contact.ContactsFragment
import com.example.mychatapp.Setting.SettingFragment
import com.example.mychatapp.data.UserMessages_Database.*
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.delay
import java.util.*
import kotlin.collections.ArrayList



class MainActivity : AppCompatActivity() {

    //////////////////////////////////////////////////////////////////
    //写完chat xml和kt之后更换下面两行的注释即可
    private lateinit var fragmentChatHistory: ChatHistoryFragment
    ///////////////////////////////////////////////////////////////////

    private lateinit var fragmentContects: ContactsFragment
    private lateinit var fragmentSetting: SettingFragment
    private lateinit var fragments:ArrayList<Fragment>
    private lateinit var tab: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var myFragmentStateAdapter: FragmentStateAdapter
    private lateinit var tabLayoutMediator: TabLayoutMediator
    private lateinit var tabConfigurationStrategy: TabLayoutMediator.TabConfigurationStrategy
    private val TAB_TEXT = arrayOf("Chat", "Friends", "Me")
    private val TAB_ICON = listOf<Int>(R.drawable.ic_chat_70x70, R.drawable.ic_friends_70x70, R.drawable.ic_me_70x70)
    private lateinit var  fragmentTransaction: FragmentTransaction



    //////////////////////////////临时设置DataBase，UI测试代码/////////////
    private lateinit var database: UserMessagesDatabase
    private lateinit var databaseDao: UserMessagesDatabaseDao
    private lateinit var repository: UserMessagesRepository
    private lateinit var viewModel: UserMessagesViewModel
    private lateinit var factory: UserMessagesViewModelFactory
    ////////////////////////////////////////////////////////////////////


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentChatHistory = ChatHistoryFragment()
        fragmentContects = ContactsFragment()
        fragmentSetting = SettingFragment()

        fragments = ArrayList()
        fragments.add(fragmentChatHistory)
        fragments.add(fragmentContects)
        fragments.add(fragmentSetting)


        tab = findViewById(R.id.tab)
        viewPager = findViewById(R.id.viewpager)
        myFragmentStateAdapter = MyFragmentsStateAdapter(this, fragments)
        viewPager.adapter = myFragmentStateAdapter



        tabConfigurationStrategy =TabLayoutMediator.TabConfigurationStrategy(){
                tab:TabLayout.Tab, position:Int ->
            tab.text = TAB_TEXT[position]
            val imageId = TAB_ICON.get(position)
            val drawable = resources.getDrawable(imageId)
            tab.icon = drawable
        }
        tabLayoutMediator   = TabLayoutMediator(tab, viewPager, tabConfigurationStrategy)
        tabLayoutMediator.attach()


        //////////////////////////////临时设置DataBasel里的一个人，UI测试代码/////////////！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
        database =  UserMessagesDatabase.getInstance(this)
        databaseDao = database.userMessagesDatabaseDao
        repository = UserMessagesRepository(databaseDao)
        factory = UserMessagesViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(UserMessagesViewModel::class.java)

        val UserMessagesOne = UserMessages()
        UserMessagesOne.acc_id = "asdaasd${System.currentTimeMillis()}"
        UserMessagesOne.name = "iKun 1"
        UserMessagesOne.email = "sfu666@sfu.ca"
        UserMessagesOne.phone = "110120119"
        UserMessagesOne.profile_pic = "res/drawable/sfu_logo.png"
        viewModel.insert(UserMessagesOne)

        val UserMessages2 = UserMessages()
        UserMessages2.acc_id = "${System.currentTimeMillis()}asdaasd"
        UserMessages2.name = "iKun 2"
        UserMessages2.email = "sfu666@sfu.ca"
        UserMessages2.phone = "110120119"
        UserMessages2.profile_pic = "res/drawable/sfu_logo.png"
        viewModel.insert(UserMessages2)

        val UserMessages3 = UserMessages()
        UserMessages3.acc_id = "poujmnkl${System.currentTimeMillis()}asdaasd"
        UserMessages3.name = "iKun 3"
        UserMessages3.email = "sfu666@sfu.ca"
        UserMessages3.phone = "110120119"
        UserMessages3.profile_pic = "res/drawable/sfu_logo.png"
        viewModel.insert(UserMessages3)

        ////////////////////////////////////////////////////////////////////
    }
}