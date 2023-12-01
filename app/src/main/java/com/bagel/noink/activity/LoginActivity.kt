package com.bagel.noink.activity

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bagel.noink.R

class LoginActivity : AppCompatActivity() {
    // 声明组件对象
    var usernameText: EditText? = null
    var passwdText: EditText? = null
    var loginButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 点击注册按钮，跳转到注册界面
        findViewById<Button>(R.id.registerButton)
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



            // 用户名与密码匹配
//            if (validLogin) {
//                MainActivity.username = username
//                MainActivity.loginState = true
//                Toast.makeText(applicationContext, "登录成功", Toast.LENGTH_SHORT)!!.show()
//                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//            } else {
//                Toast.makeText(applicationContext, "用户名或密码错误", Toast.LENGTH_SHORT)!!.show()
//            }
        }

    }
}