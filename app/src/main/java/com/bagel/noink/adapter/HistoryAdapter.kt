package com.bagel.noink.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bagel.noink.R
import com.bagel.noink.activity.DetailsActivity
import com.bagel.noink.bean.ListItemBean
import com.bagel.noink.viewholder.HistoryViewHolder
import com.bumptech.glide.Glide

class HistoryAdapter : RecyclerView.Adapter<HistoryViewHolder> {
    private var historyList: List<ListItemBean>

    constructor(historyList: List<ListItemBean>) {
        this.historyList = historyList
    }

    private fun setHistoryList(historyList: List<ListItemBean>) {
        this.historyList = historyList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.history_recycleview_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val historyItemBean = historyList[position]

        historyItemBean.title?.let {
            holder.tvTitle.text = it
        }

        historyItemBean.text?.let {
            holder.tvText.text = it
        }

        historyItemBean.coverUri?.let {
            Glide.with(holder.itemView.context)
                .load(it)
                .into(holder.ivImage)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailsActivity::class.java)
            intent.putExtra("id", historyItemBean.id)
            val imageUris = historyItemBean.imagesUri
            val uriList = ArrayList<String>()
            for (uri in imageUris) {
                uriList.add(uri.toString())
            }
            intent.putStringArrayListExtra("imageUris", uriList)
            intent.putExtra("text", historyItemBean.text)

            holder.itemView.context.startActivity(intent)
        }
    }


}