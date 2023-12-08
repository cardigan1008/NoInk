package com.bagel.noink.ui.length

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bagel.noink.R
import com.bagel.noink.databinding.EventBinding
import com.bagel.noink.databinding.FragmentEventBinding
import com.bagel.noink.databinding.FragmentLengthBinding
import com.bagel.noink.databinding.LengthBinding
import com.bagel.noink.ui.home.TextGenViewModel

class LengthFragment: Fragment() {
    private var _binding: FragmentLengthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?)
            : View? {
        // 使用 DataBindingUtil.inflate() 进行绑定
        _binding = FragmentLengthBinding.inflate(inflater,container,false)
        setNavButton()
        return binding.root
    }
    private fun setLength() {
        val includedLayout: LinearLayout = binding.linearLayout
        // 遍历 includedLayout 中的所有子视图
        val root = includedLayout.rootView
        // 遍历 GridLayout 中的每个 RelativeLayout，并获取其中的 TextView 引用
        var length: String? = String()
        for (i in 0 until includedLayout.childCount) {
            val child = includedLayout.getChildAt(i)
            if(child is com.shawnlin.numberpicker.NumberPicker){
                length += child.value.toString()
            }
        }
        if (length != null) {
            TextGenViewModel.updateLength(length)
        }
    }
    private fun setNavButton(){
        setLength()
        val navButton = binding.button
        navButton.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.nav_textEdit)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // 解除绑定
        _binding = null
    }
}