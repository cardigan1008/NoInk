package com.bagel.noink.utils

import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Email
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class RegisterHttpRequest {
    interface RegisterCallbackListener {
        fun onSuccess(responseJson: JSONObject)
        fun onFailure(errorMessage: String)
    }

    private val httpRequest = HttpRequest()

    fun registerRequest(
        username: String,
        password: String,
        gender: Boolean,
        age: Int,
        wechatId: String,
        birthday: String,
        callbackListener: RegisterCallbackListener
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
}