package com.bagel.noink.activity

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.bagel.noink.R
import com.bagel.noink.utils.Contants.Companion.UPDATE_USERNAME_PROMPT
import com.bagel.noink.utils.Contants.Companion.UPDATE_WECHATID_PROMPT

class EditInformationActivity : AppCompatActivity() {

    var updateEditText: EditText? = null
    var type: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_information)

        // 绑定组件对象
        updateEditText = findViewById(R.id.updateEditText)

        type = intent.getStringExtra("type")

        // 页面提示词
        if (type.equals("username")) {
            updateEditText?.hint = UPDATE_USERNAME_PROMPT
        } else if (type.equals("wechat")){
            updateEditText?.hint = UPDATE_WECHATID_PROMPT
        } else {
            throw Exception("error type message")
        }
    }
}