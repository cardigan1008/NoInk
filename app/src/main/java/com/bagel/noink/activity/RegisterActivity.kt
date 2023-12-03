package com.bagel.noink.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bagel.noink.R
import com.bagel.noink.utils.InformationCalc.Companion.calculateAge
import com.bagel.noink.utils.InformationCalc.Companion.convertDateFormat
import com.bagel.noink.utils.UserHttpRequest
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    // 声明组件
    var usernameText: EditText? = null
    var passwdText: EditText? = null
    var passwdText2: EditText? = null
    var wechatId: EditText? = null
    var registerButton: Button? = null
    var birthdayText: EditText? = null
    var passwordRepErrMsg: TextView? = null
    var backToLoginPrompt: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // 绑定组件对象
        usernameText = findViewById(R.id.username)
        passwdText = findViewById(R.id.Password)
        passwdText2 = findViewById(R.id.Password2)
        wechatId = findViewById(R.id.wechatId)
        registerButton = findViewById(R.id.registerButton)
        birthdayText = findViewById(R.id.editText_birthday)
        passwordRepErrMsg = findViewById(R.id.passwordRepErrMsg)
        backToLoginPrompt = findViewById(R.id.backToLoginPrompt)
        val genderRadioGroup: RadioGroup? = findViewById(R.id.radioGroup)

        // 从单选框中获得性别信息
        var gender = true
        genderRadioGroup?.setOnCheckedChangeListener { _, checkedId ->
            val radbtn = findViewById<View>(checkedId) as RadioButton
            gender = radbtn.text.equals("男")
        }


        // 验证两次输入的密码是否一致
        passwdText2?.addTextChangedListener(object : TextWatcher {
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


        // 点击注册按钮
        registerButton!!.setOnClickListener {
            // 去除空格
            val name = usernameText?.text.toString().trim { it <= ' ' }
            val passwd = passwdText?.text.toString().trim { it <= ' ' }
            val passwd2 = passwdText2?.text.toString().trim { it <= ' ' }
            val wechatId = wechatId?.text.toString().trim { it <= ' ' }
            val birthday = birthdayText?.text.toString().trim { it <= ' ' }

            // 计算年龄
            val age = calculateAge(birthday)
            val userHttpRequest = UserHttpRequest()

            // 调用sendTextRequest方法发送请求
            userHttpRequest.registerRequest(
                username = name,
                password = passwd,
                gender = gender,
                age = age,
                wechatId = wechatId,
                birthday = convertDateFormat(birthday),
                callbackListener = object : UserHttpRequest.UserCallbackListener {
                    override fun onSuccess(responseJson: JSONObject) {
                        // TODO:
                        println("Success")
                    }

                    override fun onFailure(errorMessage: String) {
                        // TODO:
                        println("Failure")
                    }
                }
            )
        }

        // 返回登录页面
        backToLoginPrompt!!.setOnClickListener { finish() }
    }

    private fun validatePassword() {
        val password = passwdText?.text.toString().trim { it <= ' ' }
        val confirmPassword = passwdText2?.text.toString().trim { it <= ' ' }

        if (password == confirmPassword) {
            passwordRepErrMsg?.visibility = View.GONE
        } else {
            passwordRepErrMsg?.visibility = View.VISIBLE
            passwordRepErrMsg?.text = "两次输入的密码不一致"
        }
    }


}