package com.example.mychatapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.mychatapp.Contact.ContactsFragment
import com.example.mychatapp.Setting.SettingFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {

    //////////////////////////////////////////////////////////////////
    //写完chat xml和kt之后更换下面两行的注释即可
    //private lateinit var fragmentChatHistory: ChatHistoryFragment
    private lateinit var fragmentChatHistory: ContactsFragment
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ///////////////////////////////////////////////////
        //写完chat xml和kt之后更换下面两行的注释即可
        //fragmentChatHistory = ChatHistoryFragment()
        fragmentChatHistory = ContactsFragment()
        ///////////////////////////////////////////////////

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

    }
}