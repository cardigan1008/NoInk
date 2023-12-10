package com.bagel.noink.adapter

import android.annotation.SuppressLint
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
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.time.Duration.Companion.days

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

        val createDate = historyItemBean.createDate
        Log.i("createDate", "${historyItemBean.createDate}")
        val dayMonthFormat = SimpleDateFormat("dd/MM", Locale.getDefault())

        createDate?.let {
            val formattedDate = dayMonthFormat.format(it)

            // 提取日和月
            val day = formattedDate.substring(0, 2)
            val month = formattedDate.substring(3) + "月"

            // 如果是同一天的第一个条目，则显示日月，否则隐藏
            if (position == 0 || formattedDate != dayMonthFormat.format(historyList[position - 1].createDate!!)) {
                holder.tvDay.visibility = View.VISIBLE
                holder.tvMonth.visibility = View.VISIBLE

                // 设置到对应的 TextView 中
                holder.tvDay.text = day
                holder.tvMonth.text = month
            } else {
                holder.tvDay.visibility = View.INVISIBLE
                holder.tvMonth.visibility = View.INVISIBLE
            }
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