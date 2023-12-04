package com.bagel.noink.utils

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
                callbackListener.onSuccess(responseJson)
            }

            override fun onFailure(errorMessage: String) {
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


}