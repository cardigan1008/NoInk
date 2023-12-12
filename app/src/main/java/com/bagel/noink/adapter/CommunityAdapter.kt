package com.bagel.noink.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bagel.noink.R
import com.bagel.noink.bean.CommunityItemBean
import com.bagel.noink.viewholder.CommunityViewHolder
import com.bagel.noink.viewholder.HistoryViewHolder

class CommunityAdapter(private var postList: List<CommunityItemBean>) : RecyclerView.Adapter<CommunityViewHolder>() {
    private fun setPostList(postList: List<CommunityItemBean>){
        this.postList = postList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_community, parent, false)
        return CommunityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        val item = postList[position]
        holder.bind(item)
    }

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return postList.size
    }
}
