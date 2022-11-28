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
        //因为acc_id不能重复，所以在第一遍运行后需要注释掉后边的代码
        //TODO:因为acc_id不能重复，所以在第一遍运行后需要注释掉后边的代码
        val UserMessagesOne = UserMessages()
        UserMessagesOne.acc_id = "XCNV_DEFSwdaSA_asefrsd"
        UserMessagesOne.name = "Ni XX"
        UserMessagesOne.email = "sfu666@sfu.ca"
        UserMessagesOne.phone = "110120119"
        UserMessagesOne.profile_pic = "res/drawable/sfu_logo.png"
        viewModel.insert(UserMessagesOne)

        val UserMessagesTwo = UserMessages()
        UserMessagesTwo.acc_id = "XCNV_DEFaasdasXFSA_asefrsd"
        UserMessagesTwo.name = "ikun"
        UserMessagesTwo.email = "sfu666@sfu.ca"
        UserMessagesTwo.phone = "110120119"
        UserMessagesTwo.profile_pic = "res/drawable/sfu_logo.png"
        viewModel.insert(UserMessagesTwo)

        val UserMessagesThree = UserMessages()
        UserMessagesThree.acc_id = "XCNV_D425XFSA_asefrsd"
        UserMessagesThree.name = "WuErFan"
        UserMessagesThree.email = "sfu666@sfu.ca"
        UserMessagesThree.phone = "110120119"
        UserMessagesThree.profile_pic = "res/drawable/sfu_logo.png"
        viewModel.insert(UserMessagesThree)


        ////////////////////////////////////////////////////////////////////


    }
}