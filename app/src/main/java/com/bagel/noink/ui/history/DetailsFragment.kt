package com.bagel.noink.ui.history

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import com.bagel.noink.R
import com.bagel.noink.bean.ListItemBean
import com.bagel.noink.databinding.FragmentDetailsBinding
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.title.text = arguments?.getParcelable<ListItemBean>("listItem")?.title
        binding.text.text = arguments?.getParcelable<ListItemBean>("listItem")?.text

        val moodTags: List<String> = arguments?.getParcelable<ListItemBean>("listItem")?.moodTags ?: emptyList()
        val eventTag: String = arguments?.getParcelable<ListItemBean>("listItem")?.eventTag ?: ""

        var tag = "#$eventTag "
        for (mood in moodTags) {
            tag = "$tag#$mood "
        }

        binding.tags.text = tag

        val createDate = arguments?.getParcelable<ListItemBean>("listItem")?.createDate
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val formattedDate = dateFormat.format(createDate)

        binding.date.text = "编辑于 $formattedDate"

        val bottomNavContainer : FrameLayout ?= activity?.findViewById(R.id.bottom_nav_container)
        bottomNavContainer?.setVisibility(View.GONE)

        var imageURIs: List<Uri> = arguments?.getParcelable<ListItemBean>("listItem")?.imagesUri ?: emptyList()
        val viewPager = binding.viewPager
        val pagerIndicator = binding.pagerIndicator

        // Set up ViewPager2 and CirclePageIndicator
        val imagePagerAdapter = ImagePagerAdapter(imageURIs)
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

    override fun onDestroy() {
        super.onDestroy()
        val bottomNavContainer : FrameLayout ?= activity?.findViewById(R.id.bottom_nav_container)
        bottomNavContainer?.setVisibility(View.VISIBLE)
    }
}