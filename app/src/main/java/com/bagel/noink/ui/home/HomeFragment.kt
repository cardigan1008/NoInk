package com.bagel.noink.ui.home

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bagel.noink.databinding.FragmentHomeBinding
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    companion object {
        private const val PICK_IMAGES_REQUEST_CODE = 101 // 更改请求码，以便处理多个图片选择
    }

    // 用于存储选择的多个图片的 Uri 列表
    private val selectedImageUris = mutableListOf<Uri>()

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
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryIntent.type = "image/*"
            galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // 允许多选图片
            startActivityForResult(galleryIntent, PICK_IMAGES_REQUEST_CODE)
        }

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGES_REQUEST_CODE && resultCode == RESULT_OK) {
            // 清空已选图片列表，以便重新添加选择的图片
            selectedImageUris.clear()

            // 获取从系统图片选择器返回的所有 Uri
            val clipData = data?.clipData
            if (clipData != null) {
                for (i in 0 until clipData.itemCount) {
                    val uri = clipData.getItemAt(i).uri
                    selectedImageUris.add(uri) // 将选择的图片 Uri 添加到列表中
                }
            } else {
                // 单选图片时处理
                val uri = data?.data
                uri?.let { selectedImageUris.add(it) }
            }

            // 处理多个图片的逻辑
            // 在此处你可以循环遍历 selectedImageUris 列表，处理每个图片的上传或其他操作

            for (imageUri in selectedImageUris) {
                Log.d("SelectedImage", "Image URI: $imageUri")
                // 这里可以添加上传图片的逻辑
                // 也可以进行其他操作，比如显示选择的图片等
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

