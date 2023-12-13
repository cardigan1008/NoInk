package com.bagel.noink.viewholder;

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bagel.noink.R
import com.bagel.noink.adapter.CommunityImageAdapter
import com.bagel.noink.bean.CommunityItemBean

class CommunityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val avatarImageView: ImageView = itemView.findViewById(R.id.avatar)
    private val contentTextView: TextView = itemView.findViewById(R.id.content)
    private val likeCountTextView: TextView = itemView.findViewById(R.id.likeNumber)
    private val commentCountTextView: TextView = itemView.findViewById(R.id.commentNumber)
    private val usernameTextView: TextView = itemView.findViewById(R.id.username)
    private val gridView: GridView = itemView.findViewById(R.id.gridView)

    fun bind(item: CommunityItemBean) {
        // 将数据绑定到视图
        Glide.with(itemView.context)
            .load(item.avatar)
            .into(avatarImageView)

        contentTextView.text = item.content
        likeCountTextView.text = item.likes.toString()
        // 评论数
        commentCountTextView.text = item.state.toString()
        usernameTextView.text = "boogiepop"

        // 如果可用，显示 imageUrls 列表中的第一个图像
        item.imageUrls?.let { imageUrls ->
            if (imageUrls.isNotEmpty()) {
                // 创建 CommunityImageAdapter 实例，并设置到 GridView 上
                val imageAdapter = CommunityImageAdapter(itemView.context, imageUrls)
                gridView.adapter = imageAdapter
            } else {
                gridView.visibility = View.GONE // 如果没有图片可用，则隐藏 GridView
            }
        } ?: run {
            gridView.visibility = View.GONE // 如果 imageUrls 为空，则隐藏 GridView
        }
    }

    companion object {
        fun create(parent: ViewGroup): CommunityViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_community, parent, false)
            return CommunityViewHolder(view)
        }
    }
}
