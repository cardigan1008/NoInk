package com.bagel.noink.ui.home
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import com.bagel.noink.R
import com.bagel.noink.databinding.FragmentTexteditBinding
import com.bagel.noink.utils.TextGenHttpRequest
import com.bumptech.glide.Glide
import org.json.JSONObject

class TextEditFragment : Fragment() {
    private var _binding: FragmentTexteditBinding? = null
    private val binding get() = _binding!!

    private lateinit var editText: EditText
    private lateinit var scrollView:ScrollView
    private val selectedImageUris = mutableListOf<Uri>()
    private lateinit var textGenHttpRequest:TextGenHttpRequest
    companion object {
        private const val PICK_IMAGES_REQUEST_CODE = 101
        private const val ARG_SELECTED_IMAGE_URIS = "selected_image_uris"
        private const val ARG_LENGTH = "length"
        private const val ARG_TYPE = "type"
        private const val ARG_ORIGIN_TEXT = "origin_text"
        private const val ARG_STYLE = "style"

        fun newInstance(
            selectedImageUris: MutableList<Uri>,
            length: String,
            type: String,
            originText: String,
            style: String
        ): TextEditFragment {
            val fragment = TextEditFragment()
            val args = Bundle()
            args.putParcelableArrayList(ARG_SELECTED_IMAGE_URIS, ArrayList(selectedImageUris))
            args.putString(ARG_LENGTH, length)
            args.putString(ARG_TYPE, type)
            args.putString(ARG_ORIGIN_TEXT, originText)
            args.putString(ARG_STYLE, style)
            fragment.arguments = args
            return fragment
        }
    }

    @SuppressLint("DiscouragedApi", "ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTexteditBinding.inflate(inflater, container, false)
        scrollView = binding.scrollView
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editText = binding.editText
        val gridLayout: GridLayout = binding.gridLayout
        val buttonRegenerate:Button = binding.buttonRegenerate
        val buttonModify:Button = binding.buttonModify
        val buttonPublish:Button = binding.buttonPublish

        val args = arguments
        selectedImageUris.addAll(args?.getParcelableArrayList<Uri>(ARG_SELECTED_IMAGE_URIS) ?: emptyList())

        val length = args?.getString(ARG_LENGTH)!!
        val type = args?.getString(ARG_TYPE)!!
        val originText = args?.getString(ARG_ORIGIN_TEXT)!!
        val style = args?.getString(ARG_STYLE)!!

        if (selectedImageUris.isNotEmpty()) {
            handleSelectedImages(selectedImageUris)
        }

        buttonRegenerate.setOnClickListener {
            generateText(length, type, originText, style)
        }

        gridLayout.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryIntent.type = "image/*"
            galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(galleryIntent, PICK_IMAGES_REQUEST_CODE)
        }
        generateText(length,type,originText,style)

    }
    private fun generateText(length: String,type: String,originText: String,style: String) {
        // 实例化TextGenHttpRequest类
        textGenHttpRequest = TextGenHttpRequest()

        // 调用sendTextRequest方法发送请求
        textGenHttpRequest.sendTextRequest(
            length = length.toInt(),
            imageUrls = selectedImageUris,
            type = type,
            originText = originText,
            style = style,
            callbackListener = object : TextGenHttpRequest.TextGenCallbackListener {
                override fun onSuccess(responseJson: JSONObject) {
                    // 处理请求成功的响应JSON对象
                    // 在这里使用responseJson
                    editText.setText(responseJson.getString("generatedText"))
                }
                override fun onFailure(errorMessage: String) {
                    // 处理请求失败
                    println("Request failed: $errorMessage")
                }
            }
        )
    }
    private fun hideKeyboard() {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(scrollView.windowToken, 0)
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
    private fun generateText(imageUris: List<Uri>){

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