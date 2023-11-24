package com.bagel.noink.ui.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bagel.noink.R

class HistoryViewHolder : RecyclerView.ViewHolder {
    var ivImage: ImageView
        private set

    var tvTitle: TextView
        private set

    var tvText: TextView
        private set

    constructor(view: View) : super(view) {
        ivImage = view.findViewById(R.id.image_recycle)
        tvTitle = view.findViewById(R.id.tv_title)
        tvText = view.findViewById(R.id.tv_text)
    }
}