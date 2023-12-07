package com.bagel.noink.ui.mood

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bagel.noink.R
import com.bagel.noink.databinding.FragmentMoodBinding
import com.bagel.noink.databinding.MoodBinding
import com.bagel.noink.ui.home.HomeViewModel
import com.bagel.noink.ui.home.TextGenViewModel
import org.w3c.dom.Text

class MoodFragment : Fragment() {
    private var _binding: FragmentMoodBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoodBinding.inflate(inflater, container, false)
        val root: View = binding.root        // 找到 TextView
        val includedLayout: View = root.findViewById(R.id.includedLayout)
        setClickListener()
        setNavButton()
        return root
    }

    private fun setClickListener() {
        val includedLayout: MoodBinding = binding.includedLayout
        // 遍历 includedLayout 中的所有子视图
        val root = includedLayout.root
        // 遍历 GridLayout 中的每个 RelativeLayout，并获取其中的 TextView 引用
        val gridLayout = root.findViewById<GridLayout>(R.id.mood_gridlayout) // 替换为您的 GridLayout ID
        for (i in 0 until gridLayout.childCount) {
            val child = gridLayout.getChildAt(i)
            if (child is RelativeLayout) {
                for(j in 0 until child.childCount){
                    val textView = child.getChildAt(j)
                    if(textView is TextView) {
                        textView.setOnClickListener {
                            val text:String = textView.text.toString()
                            TextGenViewModel.updateStyle(text)
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
            navController.navigate(R.id.nav_event)
        }
    }

}