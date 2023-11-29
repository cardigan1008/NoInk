package com.bagel.noink.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bagel.noink.R
import com.bagel.noink.databinding.FragmentTextgenerationBinding
import com.bumptech.glide.Glide

class TextGenerationFragment : Fragment() {

    private var _binding: FragmentTextgenerationBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    companion object {
        private const val PICK_IMAGES_REQUEST_CODE = 101
        private const val ARG_SELECTED_IMAGE_URIS = "selected_image_uris"

        fun newInstance(selectedImageUris: MutableList<Uri>): TextGenerationFragment {
            val fragment = TextGenerationFragment()
            val args = Bundle()
            args.putParcelableArrayList(ARG_SELECTED_IMAGE_URIS, ArrayList(selectedImageUris))
            fragment.arguments = args
            return fragment
        }
    }

    private var selectedImageUris = mutableListOf<Uri>()

    @SuppressLint("DiscouragedApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val navController = findNavController()
        _binding = FragmentTextgenerationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome

        val gridLayout: GridLayout = binding.gridLayout
        val bar: View = binding.bottomBar

        val penView: ImageView = binding.penView

        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }



        val imageResource = resources.getIdentifier(
            "ic_button_ink_bottle",
            "mipmap",
            requireActivity().packageName
        )
        if (imageResource != 0) {
            val imageView = ImageView(requireContext())
            val params = GridLayout.LayoutParams()
            params.width = 300
            params.height = 300
            imageView.layoutParams = params
            imageView.setImageResource(imageResource)
            gridLayout.addView(imageView)
        } else {
            Log.e("ImageView", "PNG image resource not found")
        }

        gridLayout.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryIntent.type = "image/*"
            galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(galleryIntent, PICK_IMAGES_REQUEST_CODE)
        }

        // 获取传递的参数
        val args = arguments
        selectedImageUris = args?.getParcelableArrayList<Uri>(ARG_SELECTED_IMAGE_URIS)!!

        if (!selectedImageUris.isNullOrEmpty()) {
            handleSelectedImages(selectedImageUris)
        }
        // 按笔尖跳转
        penView.setOnClickListener {
            val textEditFragment = TextEditFragment.newInstance(selectedImageUris)
            navController.navigate(R.id.nav_textEdit, textEditFragment.arguments)
        }
        return root
    }

    private fun handleSelectedImages(imageUris: List<Uri>) {
        val gridLayout: GridLayout = binding.gridLayout
        gridLayout.removeAllViews()

        for (imageUri in imageUris) {
            val imageView = ImageView(requireContext())
            val params = GridLayout.LayoutParams()
            params.width = 250
            params.height = 250
            imageView.layoutParams = params

            Glide.with(this)
                .load(imageUri)
                .centerCrop()
                .into(imageView)

            gridLayout.addView(imageView)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGES_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            selectedImageUris.clear()

            val clipData = data?.clipData
            if (clipData != null) {
                for (i in 0 until clipData.itemCount) {
                    val uri = clipData.getItemAt(i).uri
                    selectedImageUris.add(uri)
                }
            } else {
                val uri = data?.data
                uri?.let { selectedImageUris.add(it) }
            }

            handleSelectedImages(selectedImageUris)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}