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
        private const val PICK_IMAGE_REQUEST_CODE = 100
    }
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
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
        }


        // Customize your bar as needed
        // bar.visibility = View.VISIBLE
        // ...

        return root
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            // 处理从系统图片上传接口返回的结果
            val selectedImageUri = data?.data

            selectedImageUri?.let {

                // 获取 SharedPreferences 实例
                val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", 0)

                // 使用 SharedPreferences.Editor 保存图像 URI
                val editor = sharedPreferences.edit()
                editor.putString("imageUri", it.toString())
                editor.apply() // 应用更改
            }

            // 从 SharedPreferences 中检索图像 URI
            val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", 0)
            val savedImageUri = sharedPreferences.getString("imageUri", null)

            // 检查 URI 是否存在并加载图像
            savedImageUri?.let {
                val imageUri = Uri.parse(savedImageUri)
                binding.clickableImage.setImageURI(imageUri)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
