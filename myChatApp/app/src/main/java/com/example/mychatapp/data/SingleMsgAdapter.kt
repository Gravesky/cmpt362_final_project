package com.example.mychatapp.data

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mychatapp.R
import com.example.mychatapp.Setting.UserProfileActivity
import com.example.mychatapp.databinding.ImageMessageBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.example.mychatapp.databinding.MessageBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*

class SingleMsgAdapter (
    private val options: FirebaseRecyclerOptions<SingleMsg>,
    private val currentUserId: String?
) : FirebaseRecyclerAdapter<SingleMsg, RecyclerView.ViewHolder>(options) {
    companion object {
        const val TAG = "SingleMsgAdapter:DEBUG:"
        const val VIEW_TYPE_TEXT = 1
        const val VIEW_TYPE_IMAGE = 2
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d(TAG,"onCreateViewHolder called : viewType $viewType")
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_TEXT) {
            Log.d(TAG,"text view type")
            val view = inflater.inflate(R.layout.message, parent, false)
            val binding = MessageBinding.bind(view)
            MessageViewHolder(binding)
        } else {
            Log.d(TAG,"image view type")
            val view = inflater.inflate(R.layout.image_message, parent, false)
            val binding = ImageMessageBinding.bind(view)
            ImageMessageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        model: SingleMsg
    ) {
        Log.d(TAG,"onBindViewHolder called")
        if (options.snapshots[position].message != null) {
            Log.d(TAG,"onBindViewHolder called")
            (holder as MessageViewHolder).bind(model)
        }
        else {
            Log.d(TAG,"onBindViewHolder image")
            (holder as ImageMessageViewHolder).bind(model)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (options.snapshots[position].message != null) VIEW_TYPE_TEXT else VIEW_TYPE_IMAGE
    }

    inner class MessageViewHolder(private val binding: MessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SingleMsg) {
            Log.d(TAG,"MessageViewHolder:bind called")
            binding.messageTextView.text = item.message
            setTextColor(item.speakerID, binding.messageTextView)

            binding.messengerTextView.text = item.userName
            binding.messageTime.text = getDateTime(item.timestamp as Long)
            if (item.speakerPhotoUrl != null) {
                loadImageIntoView(binding.messengerImageView, item.speakerPhotoUrl)
            } else {
                binding.messengerImageView.setImageResource(R.drawable.ic_me_70x70)
            }
        }

        private fun setTextColor(userId: String?, textView: TextView) {
            if (currentUserId == userId && userId != null) {
                textView.setBackgroundResource(R.drawable.rounded_message_blue)
                textView.setTextColor(Color.WHITE)
            } else {
                textView.setBackgroundResource(R.drawable.rounded_message_gray)
                textView.setTextColor(Color.BLACK)
            }
        }

    }

    inner class ImageMessageViewHolder(private val binding: ImageMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SingleMsg) {
            Log.d(TAG,"ImageMessageViewHolder:bind called")
            loadImageIntoView(binding.messageImageView, item.imageUrl!!)

            binding.messengerTextView.text = item.userName
            binding.messageTime.text = getDateTime(item.timestamp as Long)
            if (item.speakerPhotoUrl != null) {
                loadImageIntoView(binding.messengerImageView, item.speakerPhotoUrl)
            } else {
                binding.messengerImageView.setImageResource(R.drawable.ic_me_70x70)
            }
        }
    }

    //ref: https://stackoverflow.com/questions/47250263/kotlin-convert-timestamp-to-datetime
    private fun getDateTime(s:Long): String? {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val netDate = Date(s)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.printStackTrace()
            "n/a"
        }
    }

    private fun loadImageIntoView(view: ImageView, url: String) {
        if (url.startsWith("gs://")) {
            val storageReference = Firebase.storage.getReferenceFromUrl(url)
            storageReference.downloadUrl
                .addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    Glide.with(view.context)
                        .load(downloadUrl)
                        .into(view)
                }
                .addOnFailureListener { e ->
                    Log.w(
                        UserProfileActivity.TAG,
                        "Getting download url was not successful.",
                        e
                    )
                }
        } else {
            Glide.with(view.context).load(url).into(view)
        }
    }

}