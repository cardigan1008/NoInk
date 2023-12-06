package com.bagel.noink.utils

import android.util.Log
import androidx.core.net.toUri
import com.bagel.noink.bean.ListItemBean
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class UserHttpRequest {
    interface UserCallbackListener {
        fun onSuccess(responseJson: JSONObject)
        fun onFailure(errorMessage: String)
    }

    private val httpRequest = HttpRequest()

    /**
     * 注册请求
     */
    fun registerRequest(
        username: String,
        password: String,
        gender: Boolean,
        age: Int,
        wechatId: String,
        birthday: String,
        callbackListener: UserCallbackListener
    ) {
        val url = Contants.SERVER_ADDRESS + "/api/user/register"
        val jsonBody = JSONObject().apply {
            put("username", username)
            put("password", password)
            put("gender", gender)
            put("age", age)
            put("wechatId", wechatId)
            put("birthday", birthday)
        }
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = jsonBody.toString().toRequestBody(mediaType)
        httpRequest.post(url, requestBody, object : HttpRequest.CallbackListener {
            override fun onSuccess(responseJson: JSONObject) {
                callbackListener.onSuccess(responseJson)
            }

            override fun onFailure(errorMessage: String) {
                callbackListener.onFailure(errorMessage)
            }
        })
    }

    /**
     * 登录请求
     */
    fun loginRequest(
        username: String,
        password: String,
        callbackListener: UserCallbackListener
    ) {
        val url = Contants.SERVER_ADDRESS + "/api/user/login"
        val jsonBody = JSONObject().apply {
            put("username", username)
            put("password", password)
        }
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = jsonBody.toString().toRequestBody(mediaType)
        httpRequest.post(url, requestBody, object : HttpRequest.CallbackListener {
            override fun onSuccess(responseJson: JSONObject) {
                Log.i("check1441",responseJson.getJSONObject("data").toString())
                callbackListener.onSuccess(responseJson)
            }

            override fun onFailure(errorMessage: String) {
                Log.i("check1441",errorMessage)
                callbackListener.onFailure(errorMessage)
            }
        })
    }

    /**
     * 修改用户信息请求
     */
    fun updateRequest(
        username: String,
        password: String,
        gender: Boolean,
        age: Int,
        wechatId: String,
        birthday: String,
        callbackListener: UserCallbackListener
    ) {
        val url = Contants.SERVER_ADDRESS + "/api/user/update"
        val jsonBody = JSONObject().apply {
            put("username", username)
            put("password", password)
            put("gender", gender)
            put("age", age)
            put("wechatId", wechatId)
            put("birthday", birthday)
        }
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = jsonBody.toString().toRequestBody(mediaType)
        httpRequest.post(url, requestBody, object : HttpRequest.CallbackListener {
            override fun onSuccess(responseJson: JSONObject) {
                callbackListener.onSuccess(responseJson)
            }

            override fun onFailure(errorMessage: String) {
                callbackListener.onFailure(errorMessage)
            }
        })
    }

    fun isUsernameExist(username: String, callback: (Boolean) -> Unit) {
        val url = Contants.SERVER_ADDRESS + "/api/user/name" + "?username=" + username

        val callbackListener = object : HttpRequest.CallbackListener {
            override fun onSuccess(responseJson: JSONObject) {
                val data = responseJson.getBoolean("data")
                callback(data)
            }

            override fun onFailure(errorMessage: String) {
            }
        }

        val httpRequest = HttpRequest()
        httpRequest.get(url, callbackListener)
    }
}