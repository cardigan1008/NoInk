package com.bagel.noink.ui.event
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bagel.noink.R
import com.bagel.noink.databinding.EventBinding
import com.bagel.noink.databinding.FragmentEventBinding
import com.bagel.noink.databinding.MoodBinding
import com.bagel.noink.ui.home.TextGenViewModel

class EventFragment : Fragment() {
    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?)
    : View? {
        // 使用 DataBindingUtil.inflate() 进行绑定
        _binding = FragmentEventBinding.inflate(inflater,container,false)
        changeText()
        setClickListener()
        setNavButton()
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    private fun changeText(){
        var textView: TextView = binding.textView
        textView.text = "是什么事情\n让你感到"+ TextGenViewModel.getStyle()+"啊？"
    }

    private fun setClickListener() {
        val includedLayout: EventBinding = binding.includedLayout
        // 遍历 includedLayout 中的所有子视图
        for (i in 0 until includedLayout.root.childCount) {
            val childView: View = includedLayout.root.getChildAt(i)
            // 检查子视图是否是 TextView
            if (childView is TextView) {
                // 在这里可以对每个 TextView 进行操作
                val textView: TextView = childView
                // 执行您的逻辑，例如设置文本或添加点击监听器等
                textView.setOnClickListener {
                    val text:String = textView.text.toString()
                    TextGenViewModel.updateType(text)
                    // 增加跳转逻辑，跳转到另一个fragment中
                }
            }
        }
    }
    private fun setNavButton(){
        val navButton: Button = binding.button
        navButton.setOnClickListener{
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