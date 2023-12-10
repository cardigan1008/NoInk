package com.bagel.noink.activity

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bagel.noink.R
import com.bagel.noink.ui.account.AccountViewModel
import com.bagel.noink.utils.Contants
import com.bagel.noink.utils.Contants.Companion.UPDATE_USERNAME_PROMPT
import com.bagel.noink.utils.Contants.Companion.UPDATE_WECHATID_PROMPT
import com.bagel.noink.utils.UserHttpRequest
import org.json.JSONObject

class EditInformationActivity : AppCompatActivity() {

    // 组件对象
    var updateEditText: EditText? = null
    var usernameMsg: TextView? = null
    var wechatIdText: EditText? = null
    var originPasswordEditText: EditText? = null
    var newPasswordEditText: EditText? = null
    var newPasswordAgainEditText: EditText? = null
    var passwordRepErrMsg: TextView? = null
    var saveButton: Button? = null

    // 页面参数类型(username, wechat or password)
    var type: String? = null

    // 成员变量
    var usernameExist = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_information)

        // 绑定组件对象
        updateEditText = findViewById(R.id.updateEditText)
        wechatIdText = findViewById(R.id.wechatIdText)
        originPasswordEditText = findViewById(R.id.origin_password)
        newPasswordEditText = findViewById(R.id.new_password)
        newPasswordAgainEditText = findViewById(R.id.new_password_again)
        passwordRepErrMsg = findViewById(R.id.passwordRepErrMsg)
        saveButton = findViewById(R.id.saveButton)
        usernameMsg = findViewById(R.id.usernameMsg)

        type = intent.getStringExtra("type")

        // 先默认全部不显示
        updateEditText?.visibility = View.GONE
        wechatIdText?.visibility = View.GONE
        originPasswordEditText?.visibility = View.GONE
        newPasswordEditText?.visibility = View.GONE
        newPasswordAgainEditText?.visibility = View.GONE

        // 调整显示的UI
        if (type.equals("username")) {
            updateEditText?.hint = UPDATE_USERNAME_PROMPT
            updateEditText?.visibility = View.VISIBLE
        } else if (type.equals("wechat")) {
            wechatIdText?.hint = UPDATE_WECHATID_PROMPT
            wechatIdText?.visibility = View.VISIBLE
        } else if (type.equals("password")) {
            originPasswordEditText?.visibility = View.VISIBLE
            newPasswordEditText?.visibility = View.VISIBLE
            newPasswordAgainEditText?.visibility = View.VISIBLE
        } else {
            throw Exception("error type message")
        }

        // 验证用户名是否重复
        updateEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No action needed
            }

            override fun afterTextChanged(s: Editable?) {
                val userHttpRequest = UserHttpRequest()
                val username = updateEditText?.text.toString().trim { it <= ' ' }
                userHttpRequest.isUsernameExist(username) {
                    if (it) {
                        if (username != "") {
                            usernameMsg?.visibility = View.VISIBLE
                        } else {
                            usernameMsg?.visibility = View.GONE
                        }
                        usernameMsg?.text = "好听的用户名已经被人取走了"
                        usernameMsg?.setTextColor(Color.RED)
                        val drawable =
                            ContextCompat.getDrawable(this@EditInformationActivity, R.drawable.lonely)
                        usernameMsg?.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            drawable,
                            null,
                            null,
                            null
                        )
                        usernameExist = true
                    } else {
                        if (username != "") {
                            usernameMsg?.visibility = View.VISIBLE
                        } else {
                            usernameMsg?.visibility = View.GONE
                        }
                        usernameMsg?.text = Contants.GOOD_USERNAME_PROMPT
                        usernameMsg?.setTextColor(Color.GREEN)
                        val drawable =
                            ContextCompat.getDrawable(this@EditInformationActivity, R.drawable.happy)
                        usernameMsg?.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            drawable,
                            null,
                            null,
                            null
                        )
                        usernameExist = false
                    }
                }
            }
        })

        // 验证两次输入的密码是否一致
        newPasswordAgainEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword()
            }

            override fun afterTextChanged(s: Editable?) {
                // No action needed
            }
        })

        // 点击保存按钮
        saveButton!!.setOnClickListener {
            // TODO: 根据不同类型事件发送不同请求
            if (type.equals("username") && !usernameExist) {
                val username = updateEditText?.text.toString().trim { it <= ' ' }
                AccountViewModel.updateUsername(username)
            }

            if (type.equals("wechat")) {
                val wechatId = wechatIdText?.text.toString().trim { it <= ' ' }
                AccountViewModel.updateWechatId(wechatId)
            }
            val userHttpRequest = UserHttpRequest()
            userHttpRequest.updateRequest(callbackListener = object :
                UserHttpRequest.UserCallbackListener {
                override fun onSuccess(responseJson: JSONObject) {
                    Log.v("USER", "Success to update user info.")
                }

                override fun onFailure(errorMessage: String) {
                    Log.v("NETWORK", "Fail to update user info.")
                }
            })

            finish()
        }
    }
    private fun validatePassword() {
        val password = newPasswordEditText?.text.toString().trim { it <= ' ' }
        val confirmPassword = newPasswordAgainEditText?.text.toString().trim { it <= ' ' }

        if (password == confirmPassword) {
            passwordRepErrMsg?.visibility = View.GONE
        } else {
            passwordRepErrMsg?.visibility = View.VISIBLE
            passwordRepErrMsg?.text = "两次输入的密码不一致"
        }
    }
}