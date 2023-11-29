package com.bagel.noink.ui.home
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import com.bagel.noink.R
import com.bagel.noink.databinding.FragmentTexteditBinding
import com.bumptech.glide.Glide

class TextEditFragment : Fragment() {
    private var _binding: FragmentTexteditBinding? = null
    private val binding get() = _binding!!

    private lateinit var editText: EditText
    private lateinit var textHome: TextView
    companion object {
        private const val PICK_IMAGES_REQUEST_CODE = 101
        private const val ARG_SELECTED_IMAGE_URIS = "selected_image_uris"

        fun newInstance(selectedImageUris: MutableList<Uri>): TextEditFragment {
            val fragment = TextEditFragment()
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
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_textedit, container, false)
        _binding = FragmentTexteditBinding.inflate(inflater, container, false)
        // Find views by their IDs
        editText = view.findViewById(R.id.editText)
        val gridLayout: GridLayout = binding.gridLayout
        val args = arguments
        selectedImageUris = args?.getParcelableArrayList<Uri>(ARG_SELECTED_IMAGE_URIS)!!

        if (!selectedImageUris.isNullOrEmpty()) {
            handleSelectedImages(selectedImageUris)
        }

        gridLayout.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryIntent.type = "image/*"
            galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(galleryIntent, PICK_IMAGES_REQUEST_CODE)
        }

        return binding.root
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


