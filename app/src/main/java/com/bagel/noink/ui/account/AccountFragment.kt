package com.bagel.noink.ui.account

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
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
    var itemUid: PersonalItemView? = null
    var itemUpdatePassword: PersonalItemView? = null

    var newGender: Boolean = true
    var newBirthday: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)
        AccountViewModel.instance = slideshowViewModel
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 实时更新用户信息显示UI
        slideshowViewModel._username.observe(viewLifecycleOwner) { newData ->
            itemUsername?.setData(newData)
        }
        slideshowViewModel._wechatId.observe(viewLifecycleOwner) { newData ->
            itemWechatId?.setData(newData)
        }
        slideshowViewModel._birthday.observe(viewLifecycleOwner) { newData ->
            itemBirthday?.setData(newData)
        }
        slideshowViewModel._gender.observe(viewLifecycleOwner) { newData ->
            itemGender?.setData(if (newData) "男" else "女")
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // 绑定相关组件
        itemUsername = activity?.findViewById(R.id.item_username)
        itemWechatId = activity?.findViewById(R.id.item_wechat)
        itemGender = activity?.findViewById(R.id.item_gender)
        itemBirthday = activity?.findViewById(R.id.item_birthday)
        itemUid = activity?.findViewById(R.id.item_uid)
        itemUpdatePassword = activity?.findViewById(R.id.item_update_password)

        // 显示用户信息
        AccountViewModel.userInfo?.let { itemUsername?.setData(it.username) }
        AccountViewModel.userInfo?.let { itemWechatId?.setData(it.wechatId) }
        AccountViewModel.userInfo?.let {
            itemGender?.setData(
                if (it.gender) "男" else "女"
            )
        }
        AccountViewModel.userInfo?.let { itemBirthday?.setData(it.birthday) }
        // AccountViewModel.userInfo?.let { itemUid?.setData(it.id.toString()) }

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

            // 点击 "确定" 按钮关闭对话框
            builder.setPositiveButton("确定") { dialog, _ ->
                newGender = tempGender
                dialog.dismiss()
            }

            // 点击 "取消" 按钮关闭对话框
            builder.setNegativeButton("取消") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

        // 修改生日弹窗
        itemBirthday?.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog =
                DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                    // 处理用户选择的日期
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(selectedYear, selectedMonth, selectedDay)

                    newBirthday = "$selectedYear-$selectedMonth-$selectedDay"

                }, year, month, day)

            // 设置最大日期
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            // 显示日期选择对话框
            datePickerDialog.show()
        }


        // 跳转到修改用户密码界面
        itemUpdatePassword?.setOnClickListener {
            val intent = Intent(this.context, EditInformationActivity::class.java)
            intent.putExtra("type", "password")
            startActivity(intent)
        }

        // TODO: 删去测试按钮代码，登录按钮和注册按钮的跳转响应
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