package com.bagel.noink.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bagel.noink.R
import com.bagel.noink.activity.DetailsActivity
import com.bagel.noink.viewholder.SearchResultViewHolder
import com.bagel.noink.bean.ListItemBean
import com.bumptech.glide.Glide


class SearchResultAdapter : RecyclerView.Adapter<SearchResultViewHolder> {

    private var searchList: List<ListItemBean>
    private val navController: NavController

    constructor(searchList: List<ListItemBean>, navController: NavController) {
        this.searchList = searchList
        this.navController = navController
    }

    fun setSearchList(searchList: List<ListItemBean>) {
        this.searchList = searchList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.search_result_recycleview_item, parent, false)
        return SearchResultViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val searchItemBean = searchList[position]

        searchItemBean.title?.let {
            holder.tvTitle.text = it
        }

        searchItemBean.text?.let {
            holder.tvText.text = it
        }

        searchItemBean.coverUri?.let {
            Glide.with(holder.itemView.context)
                .load(it)
                .into(holder.ivImage)
        }

        holder.itemView.setOnClickListener {
            val bundle = bundleOf(
                "listItem" to searchItemBean
            )
            navController.navigate(R.id.action_nav_search_result_to_nav_result_details, bundle)
        }
    }
}