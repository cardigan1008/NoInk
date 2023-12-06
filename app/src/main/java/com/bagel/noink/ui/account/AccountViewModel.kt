package com.bagel.noink.ui.account

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bagel.noink.bean.UserInfoBean
import org.json.JSONObject

class AccountViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    val text: LiveData<String> = _text

    companion object {
        var token: String? = null

        // 用户信息
        var userInfo: UserInfoBean? = UserInfoBean(
            1,
            "测试用户",
            "testPassword123",
            true,
            18,
            "13989884399",
            "2005-8-31"
            )

        fun updateUserInfoByJson(data: JSONObject) {
            userInfo?.id = data.getInt("id")
            userInfo?.age = data.getInt("age")
            userInfo?.gender = data.getBoolean("gender")
            userInfo?.username = data.getString("username")
            userInfo?.password = data.getString("password")
            userInfo?.birthday = data.getString("birthday")
            token = data.getString("tokenValue")
        }

        fun saveToken(activity: AppCompatActivity) {
            val sharedPreferences = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("token", token)
            editor.apply()
        }
    }

}