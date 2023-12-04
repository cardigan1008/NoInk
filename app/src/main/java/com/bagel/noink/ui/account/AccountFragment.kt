package com.bagel.noink.ui.account

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bagel.noink.R
import com.bagel.noink.activity.EditInformationActivity
import com.bagel.noink.activity.LoginActivity
import com.bagel.noink.activity.RegisterActivity
import com.bagel.noink.databinding.FragmentAccountBinding
import com.bagel.noink.ui.PersonalItemView

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var itemUsername: PersonalItemView? = null
    var itemWechatId: PersonalItemView? = null
    var itemGender: PersonalItemView? = null
    var itemBirthday: PersonalItemView? = null

    var newGender: Boolean = true

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
        itemUsername = activity?.findViewById(R.id.item_username)
        itemWechatId = activity?.findViewById(R.id.item_wechat)
        itemGender = activity?.findViewById(R.id.item_gender)
        itemBirthday = activity?.findViewById(R.id.item_birthday)

        // 跳转到修改用户名界面
        itemUsername?.setOnClickListener {
            val intent = Intent(this.context, EditInformationActivity::class.java)
            intent.putExtra("type", "username")
            startActivity(intent)
        }

        // 跳转到修改微信账号界面
        itemWechatId?.setOnClickListener {
            val intent = Intent(this.context, EditInformationActivity::class.java)
            intent.putExtra("type", "wechat")
            startActivity(intent)
        }

        // 弹出修改性别弹窗
        itemGender?.setOnClickListener {
            val genderOptions = arrayOf("男", "女")
            val checkedItem = 0
            var tempGender = true
            val builder = AlertDialog.Builder(this.activity)
            builder.setTitle("修改性别")
            builder.setSingleChoiceItems(genderOptions, checkedItem) { dialog, which ->
                // 处理选中的性别
                val selectedGender = genderOptions[which]
                tempGender = selectedGender == "男"
            }

            builder.setPositiveButton("确定") { dialog, which ->
                // 点击 "确定" 按钮后关闭对话框
                newGender = tempGender
                dialog.dismiss()
            }

            builder.setNegativeButton("取消") { dialog, which ->
                // 点击 "取消" 按钮后关闭对话框
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }


        // TODO: 删去测试按钮代码
        // 登录按钮和注册按钮的跳转响应
        activity?.findViewById<Button>(R.id.testButton)
            ?.setOnClickListener {
                val intent = Intent(this.context, RegisterActivity::class.java)
                startActivity(intent)
            }

        activity?.findViewById<Button>(R.id.loginButton)
            ?.setOnClickListener {
                startActivity(Intent(this.context, LoginActivity::class.java))
            }

    }
}