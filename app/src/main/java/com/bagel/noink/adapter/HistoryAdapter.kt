package com.bagel.noink.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bagel.noink.R
import com.bagel.noink.activity.DetailsActivity
import com.bagel.noink.bean.ListItemBean
import com.bagel.noink.viewholder.HistoryViewHolder

class HistoryAdapter : RecyclerView.Adapter<HistoryViewHolder> {
    private val historyList: List<ListItemBean>

    constructor(historyList: List<ListItemBean>) {
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

        historyItemBean.imageUri?.let {
            holder.ivImage.setImageURI(it)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailsActivity::class.java)
            intent.putExtra("id", historyItemBean.id)
            // TODO: delete these info when backend is ready
            intent.putExtra("image", historyItemBean.imageUri)
            intent.putExtra("text", historyItemBean.text)

            holder.itemView.context.startActivity(intent)
        }


    }


}