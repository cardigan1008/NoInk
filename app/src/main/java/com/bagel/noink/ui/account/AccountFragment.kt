package com.bagel.noink.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bagel.noink.R
import com.bagel.noink.activity.EditInformationActivity
import com.bagel.noink.activity.LoginActivity
import com.bagel.noink.activity.RegisterActivity
import com.bagel.noink.databinding.FragmentAccountBinding
import com.bagel.noink.ui.PersonalItemView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var itemUsername: PersonalItemView ?= null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // 绑定相关组件
        itemUsername = activity?.findViewById<PersonalItemView>(R.id.item_username)

        // 跳转到修改用户名界面
        itemUsername?.setOnClickListener {
            startActivity(Intent(this.context, EditInformationActivity::class.java))
        }


        // TODO: 删去测试按钮代码
        // 登录按钮和注册按钮的跳转响应
        activity?.findViewById<Button>(R.id.testButton)
            ?.setOnClickListener {
                val intent = Intent(this.context, RegisterActivity::class.java)
                intent.putExtra("type", "username")
                startActivity(intent)
            }

        activity?.findViewById<Button>(R.id.loginButton)
            ?.setOnClickListener {
                startActivity(Intent(this.context, LoginActivity::class.java))
            }

    }
}