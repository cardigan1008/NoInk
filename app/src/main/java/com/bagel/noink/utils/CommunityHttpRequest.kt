package com.bagel.noink.utils

import android.net.Uri
import com.bagel.noink.bean.CommunityItemBean
import com.bagel.noink.ui.account.AccountViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class CommunityHttpRequest {
    interface CommunityCallbackListener {
        fun onSuccess(responseJson: JSONObject)
        fun onFailure(errorMessage: String)
    }
    private val httpRequest = HttpRequest()


    fun getCommunityList(callbackListener: CommunityCallbackListener) {
        val url = Contants.SERVER_ADDRESS + "/api/article/data" // 请根据实际情况替换为正确的 API 地址
        val headerName = "satoken";
        val headerValue = AccountViewModel.token!!
        httpRequest.get(url, headerName, headerValue, object : HttpRequest.CallbackListener {
            override fun onSuccess(responseJson: JSONObject) {
                callbackListener.onSuccess(responseJson)
            }

            override fun onFailure(errorMessage: String) {
                callbackListener.onFailure(errorMessage)
            }
        })
    }
    fun getCommunityDetail(aid: String, callbackListener: CommunityCallbackListener){
        val url = Contants.SERVER_ADDRESS + "/api/article/detail"
        val headerName = "satoken";
        val headerValue = AccountViewModel.token!!
        val params = mapOf(
            "aid" to aid,
        )
        httpRequest.get(url, params, headerName, headerValue, object : HttpRequest.CallbackListener {
            override fun onSuccess(responseJson: JSONObject) {
                callbackListener.onSuccess(responseJson)
            }

            override fun onFailure(errorMessage: String) {
                callbackListener.onFailure(errorMessage)
            }
        })

    }
    fun addLikes(aid: String, callbackListener: CommunityCallbackListener){
        val url = Contants.SERVER_ADDRESS +"/api/article/like"
        val headerName = "satoken";
        val headerValue = AccountViewModel.token!!
        val params = mapOf(
            "aid" to aid,
        )
        httpRequest.get(url, params, headerName, headerValue, object : HttpRequest.CallbackListener{
            override fun onSuccess(responseJson: JSONObject) {
                callbackListener.onSuccess(responseJson)
            }

            override fun onFailure(errorMessage: String) {
                callbackListener.onFailure(errorMessage)
            }
        })
    }
    fun addCommentLikes(cid : String, callbackListener: CommunityCallbackListener){
        val url = Contants.SERVER_ADDRESS + "/api/comment/like"
        val headerName = "satoken";
        val headerValue = AccountViewModel.token!!;
        val jsonBody = JSONObject().apply {
            put("cid", cid)
        }
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = jsonBody.toString().toRequestBody(mediaType)
        httpRequest.post(url, requestBody, headerName, headerValue, object : HttpRequest.CallbackListener {
            override fun onSuccess(responseJson: JSONObject) {
                callbackListener.onSuccess(responseJson)
            }
            override fun onFailure(errorMessage: String) {
                callbackListener.onFailure(errorMessage)
            }
        })
    }


}