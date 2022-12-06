package com.example.mychatapp.tool

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mychatapp.data.SingleMsgAdapter

class MyScrollToBottomObserver(
    private val recycler: RecyclerView,
    private val adapter: SingleMsgAdapter,
    private val manager: LinearLayoutManager
) : RecyclerView.AdapterDataObserver() {
    companion object {
        const val TAG = "ScrollTool:DEBUG:"
    }
    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        Log.d(TAG,"onItemRangeInserted called: start $positionStart total $itemCount")
        super.onItemRangeInserted(positionStart, itemCount)
        val count = adapter.itemCount
        val lastVisiblePosition = manager.findLastCompletelyVisibleItemPosition()
        // If the recycler view is initially being loaded or the
        // user is at the bottom of the list, scroll to the bottom
        // of the list to show the newly added message.
        val loading = lastVisiblePosition == -1
        val atBottom = positionStart >= count - 1 && lastVisiblePosition == positionStart - 1
        if (loading || atBottom) {
            Log.d(TAG,"setting scroller position $positionStart")
            recycler.scrollToPosition(positionStart)
        }
    }
}