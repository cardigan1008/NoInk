package com.bagel.noink.activity

import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bagel.noink.R
import com.bagel.noink.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val text = intent.getStringExtra("text")
        val imageURI = intent.getParcelableExtra<Uri>("image")

        findViewById<TextView>(R.id.textView).text = text
        findViewById<ViewPager>(R.id.viewPager).adapter = imageURI?.let { ImagePagerAdapter(it) }

        binding.back.setOnClickListener { finish() }
    }

    private inner class ImagePagerAdapter(private val imageUri: Uri) : PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val imageView = ImageView(container.context)
            imageView.setImageURI(imageUri)
            container.addView(imageView)
            return imageView
        }

        override fun getCount(): Int {
            return 1
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

    }
}