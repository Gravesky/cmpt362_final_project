package com.example.mychatapp.Contact

import android.content.Context
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.mychatapp.R
import com.example.mychatapp.Util
import com.example.mychatapp.data.UserMessages_Database.UserMessages
import java.io.File

class MyContactsListAdapter(private val context: Context, private var list: List<UserMessages>): BaseAdapter() {

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
        var imageViewPortrait = view.findViewById<ImageView>(R.id.contactProtrait)
        var textViewName = view.findViewById<TextView>(R.id.contactName)

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


        //

        val name = list.get(position).name
        textViewName.text = name
        return view
    }

    fun replaceList(newList:List<UserMessages>){
        list = newList
    }

    fun getTheId(position: Int): String {
        return list.get(position).acc_id
    }
    fun getTheName(position: Int): String {
        return list.get(position).name
    }
    fun getTheEmail(position: Int): String {
        return list.get(position).email
    }

}