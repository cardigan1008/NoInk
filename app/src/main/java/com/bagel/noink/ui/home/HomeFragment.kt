package com.bagel.noink.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bagel.noink.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        val imageView: ImageView = binding.clickableImage
        val bar: View = binding.bottomBar

        // Set text from ViewModel
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        // Set OnClickListener for the image
        imageView.setOnClickListener {
            // Handle click action here
        }

        // Customize your bar as needed
        // bar.visibility = View.VISIBLE
        // ...

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
