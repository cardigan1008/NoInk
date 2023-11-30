package com.bagel.noink.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bagel.noink.R
import com.bagel.noink.activity.DetailsActivity
import com.bagel.noink.bean.ListItemBean
import com.bagel.noink.viewholder.StarsViewHolder

class StarsAdapter : RecyclerView.Adapter<StarsViewHolder> {

    private val starsList : List<ListItemBean>

    constructor(starsList: List<ListItemBean>) {
        this.starsList = starsList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StarsViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.stars_recycleview_item, parent, false)
        return StarsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return starsList.size
    }

    override fun onBindViewHolder(holder: StarsViewHolder, position: Int) {
        val starsItemBean = starsList[position]

        starsItemBean.title?.let {
            holder.tvTitle.text = it
        }

        starsItemBean.text?.let {
            holder.tvText.text = it
        }

        starsItemBean.imageUri?.let {
            holder.ivImage.setImageURI(it)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailsActivity::class.java)
            intent.putExtra("id", starsItemBean.id)
            // TODO: delete these info when backend is ready
            intent.putExtra("image", starsItemBean.imageUri)
            intent.putExtra("text", starsItemBean.text)

            holder.itemView.context.startActivity(intent)
        }
    }


}