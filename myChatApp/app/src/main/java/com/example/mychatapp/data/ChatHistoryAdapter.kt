package com.example.mychatapp.data

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mychatapp.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.example.mychatapp.databinding.ImageMessageBinding
import com.example.mychatapp.databinding.ChatBinding

class ChatHistoryAdapter(
    private val options: FirebaseRecyclerOptions<SingleMsg>,
    private val currentUserName: String?
): FirebaseRecyclerAdapter<SingleMsg, RecyclerView.ViewHolder>(options) {
    companion object {
        const val TAG = "ChatAdapter:DEBUG:"
        const val VIEW_TYPE_TEXT = 1
        const val VIEW_TYPE_IMAGE = 2
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d(TAG,"onCreateViewHolder called")
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.chat, parent, false)
        val binding = ChatBinding.bind(view)
        Log.d(TAG,"onCreateViewHolder finished")
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        model: SingleMsg
    ) {
        Log.d(TAG,"onBindViewHolder called")
        //TODO：这里
//        if (options.snapshots[position].text != null) {
//            (holder as ChatViewHolder).bind(model)
//        }
        Log.d(TAG,"onBindViewHolder finished")
    }

    inner class ChatViewHolder(private val binding: ChatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SingleMsg) {
            //TODO:这里
//            binding.messengerTextView.text = item.userName
//            Log.d(TAG,"Listener: ${item.userName}")
//            //setTextColor(item.name, binding.messageTextView)
//
//            binding.latestMsgTextView.text = item.message
            //TODO: add image / photo ability
//            if (item.photoUrl != null) {
//                loadImageIntoView(binding.messengerImageView, item.photoUrl!!)
//            } else {
//                binding.messengerImageView.setImageResource(R.drawable.ic_account_circle_black_36dp)
//            }
        }


//        private fun setTextColor(userName: String?, textView: TextView) {
//            if (userName != ANONYMOUS && currentUserName == userName && userName != null) {
//                textView.setBackgroundResource(R.drawable.rounded_message_blue)
//                textView.setTextColor(Color.WHITE)
//            } else {
//                textView.setBackgroundResource(R.drawable.rounded_message_gray)
//                textView.setTextColor(Color.BLACK)
//            }
//        }
    }
}