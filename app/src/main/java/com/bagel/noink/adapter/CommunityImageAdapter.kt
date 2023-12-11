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

    private val positionToImageViewMap = mapOf(
        0 to R.id.photo1,
        1 to R.id.photo2,
        2 to R.id.photo3,
        3 to R.id.photo4,
        4 to R.id.photo5,
        6 to R.id.photo6,
        7 to R.id.photo7,
        8 to R.id.photo8,
        9 to R.id.photo9,
    )

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

        assert(position <= 9)
        val imageView: ImageView? = positionToImageViewMap[position]?.let { view.findViewById(it) }
        imageView?.setImageDrawable(images[position])

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