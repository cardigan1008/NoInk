package com.bagel.noink.ui.generate
import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bagel.noink.R
import com.bagel.noink.databinding.EventBinding
import com.bagel.noink.databinding.FragmentEventBinding

class EventFragment : Fragment() {
    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!

    private lateinit var defaultBackground: Drawable
    private lateinit var pressedBackground: Drawable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?)
    : View? {
        // 使用 DataBindingUtil.inflate() 进行绑定
        _binding = FragmentEventBinding.inflate(inflater,container,false)
        defaultBackground = requireContext().resources.getDrawable(R.drawable.default_background)
        pressedBackground = requireContext().resources.getDrawable(R.drawable.pressed_background)

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
        val root = includedLayout.root
        // 遍历 GridLayout 中的每个 RelativeLayout，并获取其中的 TextView 引用
        val gridLayout = root.findViewById<GridLayout>(R.id.event_gridlayout) // 替换为您的 GridLayout ID
        for (i in 0 until gridLayout.childCount) {
            val child = gridLayout.getChildAt(i)
            if (child is RelativeLayout) {
                for(j in 0 until child.childCount){
                    val textView = child.getChildAt(j)
                    if(textView is ToggleButton) {
                        textView.setOnCheckedChangeListener { _, isChecked ->
                            if (isChecked) {
                                textView.background = pressedBackground
                                val text:String = textView.text.toString()
                                TextGenViewModel.updateType(text)
                            } else {
                                textView.background = defaultBackground
                            }
                        }
                    }
                }
            }
        }
    }
    private fun setNavButton(){
        val navButton: Button = binding.button
        navButton.setOnClickListener{
            val navController = findNavController()
            navController.navigate(R.id.nav_length)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // 解除绑定
        _binding = null
    }
}