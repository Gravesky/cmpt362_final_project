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
import com.example.mychatapp.databinding.MessageBinding

class SingleMsgAdapter (
    private val options: FirebaseRecyclerOptions<SingleMsg>,
    private val currentUserName: String?
) : FirebaseRecyclerAdapter<SingleMsg, RecyclerView.ViewHolder>(options) {
    companion object {
        const val TAG = "SingleMsgAdapter:DEBUG:"
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d(TAG,"onCreateViewHolder called ")
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.message, parent, false)
        val binding = MessageBinding.bind(view)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        model: SingleMsg
    ) {
        Log.d(TAG,"onBindViewHolder called")
        if (options.snapshots[position].message != null) {
            (holder as MessageViewHolder).bind(model)
        }
//        else {
//            (holder as ImageMessageViewHolder).bind(model)
//        }
    }

    inner class MessageViewHolder(private val binding: MessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SingleMsg) {
            Log.d(TAG,"MessageViewHolder:bind called")
            binding.messageTextView.text = item.message
            setTextColor(item.userName, binding.messageTextView)

            binding.messengerTextView.text = item.userName
//            if (item.photoUrl != null) {
//                loadImageIntoView(binding.messengerImageView, item.photoUrl)
//            } else {
//                binding.messengerImageView.setImageResource(R.drawable.ic_account_circle_black_36dp)
//            }
        }

        private fun setTextColor(userName: String?, textView: TextView) {
            if (currentUserName == userName && userName != null) {
                textView.setBackgroundResource(R.drawable.rounded_message_blue)
                textView.setTextColor(Color.WHITE)
            } else {
                textView.setBackgroundResource(R.drawable.rounded_message_gray)
                textView.setTextColor(Color.BLACK)
            }
        }

    }

}