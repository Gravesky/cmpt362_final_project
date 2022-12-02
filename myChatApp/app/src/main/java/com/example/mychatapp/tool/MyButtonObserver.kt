package com.example.mychatapp.tool

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ImageView
import com.example.mychatapp.R

class MyButtonObserver(private val button: ImageView) : TextWatcher {
    companion object {
        const val TAG = "MyButtonObserver:DEBUG:"
    }
    override fun onTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {
        Log.d(TAG,"onTextChanged called, button.isEnabled = ${button.isEnabled}")
        if (charSequence.toString().trim().isNotEmpty()) {
            button.isEnabled = true
            button.setImageResource(R.drawable.outline_send_24)
            Log.d(TAG,"Button enabled")
        } else {
            button.isEnabled = false
            button.setImageResource(R.drawable.outline_send_gray_24)
            Log.d(TAG,"Button disabled")
        }
    }

    override fun beforeTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {}
    override fun afterTextChanged(editable: Editable) {}
}