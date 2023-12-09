package com.bagel.noink.ui.account

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bagel.noink.bean.UserInfoBean
import org.json.JSONObject

class AccountViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    val text: LiveData<String> = _text

    val _userInformation = MutableLiveData<UserInfoBean>()
    val _username = MutableLiveData<String>()
    val _gender = MutableLiveData<Boolean>()
    val _birthday = MutableLiveData<String>()
    val _wechatId = MutableLiveData<String>()
    val _uid = MutableLiveData<Long>()


    companion object {
        var token: String? = "token"
        var instance: AccountViewModel? = null


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
            userInfo?.id = data.getLong("uid")
            userInfo?.age = data.getInt("age")
            userInfo?.gender = data.getBoolean("gender")
            userInfo?.username = data.getString("username")
            userInfo?.password = data.getString("password")
            userInfo?.birthday = data.getString("birthday")
            userInfo?.wechatId = data.getString("wechatId")

            token = data.getString("tokenValue")
            if (instance != null) {
                instance!!._userInformation.value = userInfo
                instance!!._username.value = userInfo?.username
                instance!!._gender.value = userInfo?.gender
                instance!!._birthday.value = userInfo?.birthday
                instance!!._wechatId.value = userInfo?.wechatId
                instance!!._uid.value = userInfo?.id
            }
        }

        fun updateUserInfo(newInfo: UserInfoBean) {
            userInfo = newInfo
            if (instance != null) {
                instance!!._userInformation.value = userInfo
            }
        }

        fun saveToken(activity: AppCompatActivity) {
            val sharedPreferences = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("token", token)
            editor.apply()
        }
    }

}