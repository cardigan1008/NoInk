package com.bagel.noink.activity

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import com.bagel.noink.databinding.ActivityDetailsBinding
import com.bumptech.glide.Glide

class DetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val text = intent.getStringExtra("text")
        val imageURIs = convertToUriList(intent.getStringArrayListExtra("imageUris"))

        binding.text.text = text
        val viewPager = binding.viewPager
        val pagerIndicator = binding.pagerIndicator

        val imagePagerAdapter = imageURIs?.let { ImagePagerAdapter(it) }
        viewPager.adapter = imagePagerAdapter
        pagerIndicator.setViewPager(viewPager)
    }

    private fun convertToUriList(imageURIs: List<String>?): List<Uri>? {
        return imageURIs?.map { Uri.parse(it) }
    }

    private inner class ImagePagerAdapter(private val imageURIs: List<Uri>) : PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val imageView = ImageView(container.context)
            Glide.with(container)
                .load(imageURIs[position])
                .into(imageView)
            container.addView(imageView)
            return imageView
        }

        override fun getCount(): Int {
            return imageURIs.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }
}