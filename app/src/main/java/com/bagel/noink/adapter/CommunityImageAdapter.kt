package com.bagel.noink.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bagel.noink.R

class CommunityImageAdapter(private val context: Context, private val images: List<Drawable>) : BaseAdapter() {

    override fun getCount(): Int {
        return images.size
    }

    override fun getItem(position: Int): Any {
        return images[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = if (convertView == null) {
            LayoutInflater.from(context).inflate(getLayoutResource(), parent, false)
        } else {
            convertView
        }

        // TODO: 这里可能需要修改
        val imageView: ImageView = view.findViewById(R.id.imageView)
        imageView.setImageDrawable(images[position])

        return view
    }

    private fun getLayoutResource(): Int {
        return if (images.size <= 4) {
            R.layout.grid_item_four
        } else {
            R.layout.grid_item_nine
        }
    }
}