package com.bagel.noink.ui.textedit

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bagel.noink.R
import com.bagel.noink.adapter.ImageAdapter
import com.bagel.noink.databinding.FragmentTexteditBinding
import com.bagel.noink.ui.home.HomeFragment
import com.bagel.noink.ui.home.TextGenViewModel
import com.bagel.noink.ui.home.TextGenerationFragment
import com.bagel.noink.utils.TextGenHttpRequest
import org.json.JSONObject
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class TextEditFragment: Fragment() {
    private var _binding: FragmentTexteditBinding? = null
    private val binding get() = _binding!!
    companion object {
        private const val PICK_IMAGES_REQUEST_CODE = 101
        private const val ARG_SELECTED_IMAGE_URIS = "selected_image_uris"
    }
    private var selectedImageUris = mutableListOf<Uri>()
    private lateinit var textGenHttpRequest: TextGenHttpRequest
    // save vars
    private lateinit var createdAt: String
    private lateinit var updatedAt: String
    private lateinit var generatedText: String
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?)
            : View? {
        // 使用 DataBindingUtil.inflate() 进行绑定
        _binding = FragmentTexteditBinding.inflate(inflater,container,false)
        textGenHttpRequest = TextGenHttpRequest()
        setImageUploadButton()
        setGenButton()
        setSaveButton()
        return binding.root
    }

    private fun setImageUploadButton(){
        val imageView = binding.imageView
        imageView.setOnClickListener{
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryIntent.type = "image/*"
            galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(galleryIntent, TextEditFragment.PICK_IMAGES_REQUEST_CODE)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == TextEditFragment.PICK_IMAGES_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // 清空已选图片列表，以便重新添加选择的图片
            selectedImageUris.clear()
            // 获取从系统图片选择器返回的所有 Uri
            val clipData = data?.clipData
            if (clipData != null) {
                for (i in 0 until clipData.itemCount) {
                    val uri = clipData.getItemAt(i).uri
                    val projection = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor: Cursor? = requireActivity().contentResolver.query(uri, projection, null, null, null)
                    var filePath: String
                    cursor?.use {
                        if (it.moveToFirst()) {
                            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                            filePath = it.getString(columnIndex)
                            // filePath 变量包含实际的本地文件路径
//                            val aliyunOSSUrl = aliyunOSSManager.uploadImage(filePath,"test"+ generateRandomString(6));
                            val aliyunOSSUrl = "https://cardigan1008.oss-cn-hangzhou.aliyuncs.com/test"
                            if(aliyunOSSUrl != null){
                                selectedImageUris.add(Uri.parse(aliyunOSSUrl))
                            }
                            // 将选择的图片 Uri 添加到列表中
                        }
                    }
                }
            } else {
                // 单选图片时处理
                val uri = data?.data!!
                val projection = arrayOf(MediaStore.Images.Media.DATA)
                val cursor: Cursor? = requireActivity().contentResolver.query(uri, projection, null, null, null)
                var filePath: String
                cursor?.use {
                    if (it.moveToFirst()) {
                        val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                        filePath = it.getString(columnIndex)
                        // filePath 变量包含实际的本地文件路径

//                        val aliyunOSSUrl = aliyunOSSManager.uploadImage(filePath,"test");
                        val aliyunOSSUrl = "https://cardigan1008.oss-cn-hangzhou.aliyuncs.com/test"
                        if(aliyunOSSUrl != null){
                            selectedImageUris.add(Uri.parse(aliyunOSSUrl))
                        }
                        // 将选择的图片 Uri 添加到列表中
                    }
                }
            }
        }
        handleSelectedImages(selectedImageUris)
    }
    private fun handleSelectedImages(imageUris: List<Uri>) {
        val recyclerView: RecyclerView = binding.recyclerView

        val adapter = ImageAdapter(imageUris)
        recyclerView.adapter = adapter

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
    }
    private fun setGenButton(){
        val genButton = binding.buttonRegenerate
        genButton.setOnClickListener {
            generateText()
        }
    }
    private fun generateText() {
        // 实例化TextGenHttpRequest类
        val editText = binding.editText
        TextGenViewModel.updateOriginText(editText.text.toString())
        TextGenViewModel.updateInfoUrls(selectedImageUris)
        // 得到参数
        val length = TextGenViewModel.getLength()!!
        val typeStr = TextGenViewModel.getType()!!
        var origintext = TextGenViewModel.getOriginText()!!
        var style = TextGenViewModel.getStyle()!!

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
                    generatedText = responseJson.getString("generatedText");
                    TextGenViewModel.updateOriginText(responseJson.getString("originText"))
                    TextGenViewModel.updateType(responseJson.getString("type"))
                }
                override fun onFailure(errorMessage: String) {
                    // 处理请求失败
                    println("Request failed: $errorMessage")
                }
            }
        )
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setSaveButton(){
        val saveButton = binding.buttonSave
        saveButton.setOnClickListener {
            saveText()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentTime(): String {
        val currentTime = ZonedDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        return currentTime.format(formatter)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveText(){
        textGenHttpRequest.sendSaveRequest(
            createdAt = createdAt,
            updatedAt = getCurrentTime(),
            originText = TextGenViewModel.getOriginText()!!,
            imageUrls = selectedImageUris,
            labels = TextGenViewModel.getStyle()!!,
            generatedText = generatedText,
            type = TextGenViewModel.getType()!!,
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
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}