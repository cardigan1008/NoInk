package com.bagel.noink.activity

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bagel.noink.R
import com.bagel.noink.ui.account.AccountViewModel
import com.bagel.noink.utils.InformationCalc
import com.bagel.noink.utils.UserHttpRequest
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    // 声明组件对象
    var usernameText: EditText? = null
    var passwdText: EditText? = null
    var loginButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 点击注册按钮，跳转到注册界面
        findViewById<TextView>(R.id.registerPrompt)
            .setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }

        // 绑定组件对象
        usernameText = findViewById(R.id.username)
        passwdText = findViewById(R.id.Password)
        loginButton = findViewById(R.id.loginButton)

        // 按下登录按钮
        loginButton?.setOnClickListener {
            val username = usernameText!!.text.toString()
            val passwd = passwdText!!.text.toString()

            val userHttpRequest = UserHttpRequest()
            userHttpRequest.loginRequest(
                username = username,
                password = passwd,
                callbackListener = object : UserHttpRequest.UserCallbackListener {
                    override fun onSuccess(responseJson: JSONObject) {
                        val data = responseJson.getJSONObject("data")
                        AccountViewModel.updateUserInfoByJson(data)
                        AccountViewModel.saveToken(this@LoginActivity)
                        // 登录成功，返回上一个Activity
                        finish()
                    }

                    override fun onFailure(errorMessage: String) {
                        // TODO:
                        println("Failure")
                    }
                }
            )
        }

    }
}