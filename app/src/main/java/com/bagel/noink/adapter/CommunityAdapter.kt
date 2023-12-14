package com.bagel.noink.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bagel.noink.R
import com.bagel.noink.bean.CommunityItemBean
import com.bagel.noink.viewholder.CommunityViewHolder
import com.bagel.noink.viewholder.HistoryViewHolder

class CommunityAdapter(private var postList: List<CommunityItemBean>, private var navController: NavController) : RecyclerView.Adapter<CommunityViewHolder>() {
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
        holder.itemView.setOnClickListener {
            val bundle = bundleOf(
                "aid" to item.aid.toString()
            )
            navController.navigate(R.id.action_nav_community_to_nav_community_detail, bundle)
        }
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
