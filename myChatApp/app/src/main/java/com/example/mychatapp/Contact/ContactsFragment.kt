package com.example.mychatapp.Contact

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mychatapp.R
import com.example.mychatapp.data.UserMessages_Database.*

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


    private lateinit var view1: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)

        myListView = view.findViewById(R.id.runHistoryListView)
        database =  UserMessagesDatabase.getInstance(requireActivity())
        databaseDao = database.userMessagesDatabaseDao
        repository = UserMessagesRepository(databaseDao)
        factory = UserMessagesViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity(), factory).get(UserMessagesViewModel::class.java)

        arrayList = ArrayList()
        myAdapter = MyContactsListAdapter(requireActivity(),arrayList)
        myListView.adapter = myAdapter

        viewModel.allUserMessagesLiveData.observe(requireActivity()){
            println("debug: loading historyList")
            myAdapter.replaceList(it)
            println("debug: loading historyList2")
            myAdapter.notifyDataSetChanged()
            println("debug: loading historyList3")
        }
/*
*/
        myListView.setOnItemClickListener {parent,view,position,id->

            val jumpToShow:Intent = Intent(context, ContactActivity::class.java)


            startActivity(jumpToShow)

        }

        return view
    }

    override fun onResume() {
        super.onResume()
        myAdapter.notifyDataSetChanged()
    }

}