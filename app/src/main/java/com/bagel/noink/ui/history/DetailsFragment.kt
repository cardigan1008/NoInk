package com.bagel.noink.ui.history

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import com.bagel.noink.R
import com.bagel.noink.databinding.FragmentDetailsBinding
import com.bagel.noink.databinding.FragmentPostBinding
import com.bumptech.glide.Glide

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var imageUris: ArrayList<Uri> = ArrayList()

        val viewPager = binding.viewPager
        val pagerIndicator = binding.pagerIndicator

        // Set up ViewPager2 and CirclePageIndicator
        val imagePagerAdapter = ImagePagerAdapter(imageUris)
        viewPager.adapter = imagePagerAdapter
        pagerIndicator.setViewPager(viewPager)

        return root
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