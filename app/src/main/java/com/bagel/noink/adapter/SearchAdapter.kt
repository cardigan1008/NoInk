package com.bagel.noink.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bagel.noink.R
import com.bagel.noink.activity.DetailsActivity
import com.bagel.noink.viewholder.SearchViewHolder
import com.bagel.noink.bean.ListItemBean
import com.bumptech.glide.Glide


class SearchAdapter : RecyclerView.Adapter<SearchViewHolder> {

    private var searchList: List<ListItemBean>

    constructor(searchList: List<ListItemBean>) {
        this.searchList = searchList
    }

    fun setSearchList(searchList: List<ListItemBean>) {
        this.searchList = searchList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.search_result_recycleview_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
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
            val intent = Intent(holder.itemView.context, DetailsActivity::class.java)
            intent.putExtra("id", searchItemBean.id)
            val imageUris = searchItemBean.imagesUri
            val uriList = ArrayList<String>()
            for (uri in imageUris) {
                uriList.add(uri.toString())
            }
            intent.putStringArrayListExtra("imageUris", uriList)
            intent.putExtra("text", searchItemBean.text)
            holder.itemView.context.startActivity(intent)
        }
    }
}