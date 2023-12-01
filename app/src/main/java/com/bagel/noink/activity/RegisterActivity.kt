package com.bagel.noink.activity

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bagel.noink.R

class RegisterActivity : AppCompatActivity() {
    // 声明组件
    var usernameText: EditText? = null
    var passwdText: EditText? = null
    var passwdText2: EditText? = null
    var registerButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // 绑定组件对象
        usernameText = findViewById(R.id.username)
        passwdText = findViewById(R.id.Password)
        passwdText2 = findViewById(R.id.Password2)
        registerButton = findViewById(R.id.registerButton)

        // 点击注册按钮
        registerButton!!.setOnClickListener {
            // 去除空格
            val name = usernameText?.text.toString().trim { it <= ' ' }
            val passwd = passwdText?.text.toString().trim { it <= ' ' }
            val passwd2 = passwdText2?.text.toString().trim { it <= ' ' }

            // 判断用户是否已经存在
//            if (isUserExist(name)) {
//                Toast.makeText(this, "该用户名已经存在", Toast.LENGTH_LONG).show()
//            } else if (passwd != passwd2) {
//                Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_LONG).show()
//            } else {
//                // TODO: connect to backend
//            }
        }
    }

}