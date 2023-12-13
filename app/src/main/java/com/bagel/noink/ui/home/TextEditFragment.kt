package com.bagel.noink.ui.home
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bagel.noink.adapter.ImageAdapter
import com.bagel.noink.databinding.FragmentTexteditBinding
import com.bagel.noink.ui.textedit.TextGenViewModel
import com.bagel.noink.utils.TextGenHttpRequest
import org.json.JSONObject
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class TextEditFragment : Fragment() {
    private var _binding: FragmentTexteditBinding? = null
    private val binding get() = _binding!!

    private lateinit var editText: EditText
    private lateinit var scrollView:ScrollView
    private val selectedImageUris = mutableListOf<Uri>()
    private lateinit var textGenHttpRequest:TextGenHttpRequest

    private lateinit var createdAt: String
    private lateinit var updatedAt: String
    private lateinit var originText: String
    private lateinit var generatedText: String
    private lateinit var type: String

    companion object {
        private const val PICK_IMAGES_REQUEST_CODE = 101
        private const val ARG_SELECTED_IMAGE_URIS = "selected_image_uris"
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

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editText = binding.editText
        val gridLayout: RecyclerView = binding.recyclerView
        val buttonRegenerate:Button = binding.buttonRegenerate
        val buttonSave:Button = binding.buttonSave

        val args = arguments
        selectedImageUris.addAll(args?.getParcelableArrayList<Uri>(ARG_SELECTED_IMAGE_URIS) ?: emptyList())

        val length = TextGenViewModel.getLength()!!
        val type = TextGenViewModel.getType()!!
        val originText = TextGenViewModel.getOriginText()!!
        val style = TextGenViewModel.getStyle()!!

        textGenHttpRequest = TextGenHttpRequest()
        if (selectedImageUris.isNotEmpty()) {
            handleSelectedImages(selectedImageUris)
        }

        buttonRegenerate.setOnClickListener {
            generateText(length, type, originText, style)
        }
        buttonSave.setOnClickListener {
            saveText(selectedImageUris, style)
        }
        gridLayout.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryIntent.type = "image/*"
            galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(galleryIntent, PICK_IMAGES_REQUEST_CODE)
        }
        generateText(length,type,originText,style)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentTime(): String {
        val currentTime = ZonedDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        return currentTime.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveText(imageUris: List<Uri>, labels: String){
        textGenHttpRequest.sendSaveRequest(
            createdAt = createdAt,
            updatedAt = getCurrentTime(),
            originText = originText,
            imageUrls = imageUris,
            labels = labels,
            generatedText = generatedText,
            type = type,
            callbackListener = object : TextGenHttpRequest.TextGenCallbackListener {
                override fun onSuccess(responseJson: JSONObject) {
                    // 处理请求成功的响应JSON对象
                    // 在这里使用responseJson
                    Log.i("save succuss", responseJson.getString("code"));
              }
                override fun onFailure(errorMessage: String) {
                    // 处理请求失败
                    println("Request failed: $errorMessage")
                }
            }
        )


    }
    private fun generateText(length: String,typeStr: String,origintext: String,style: String) {
        // 实例化TextGenHttpRequest类

        // 调用sendTextRequest方法发送请求
        textGenHttpRequest.sendTextRequest(
            length = length.toInt(),
            imageUrls = selectedImageUris,
            type = typeStr,
            originText = origintext,
            style = style,
            callbackListener = object : TextGenHttpRequest.TextGenCallbackListener {
                override fun onSuccess(responseJson: JSONObject) {
                    // 处理请求成功的响应JSON对象
                    // 在这里使用responseJson
                    editText.setText(responseJson.getString("generatedText"))
                    createdAt = responseJson.getString("createdAt")
                    updatedAt = responseJson.getString("updatedAt")
                    originText = responseJson.getString("originText")
                    generatedText = responseJson.getString("generatedText");
                    type = responseJson.getString("type");
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
        val recyclerView: RecyclerView = binding.recyclerView

        val adapter = ImageAdapter(imageUris)
        recyclerView.adapter = adapter

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
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