package com.bagel.noink.utils

import android.net.Uri
import com.bagel.noink.ui.account.AccountViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject


class TextGenHttpRequest {

    interface TextGenCallbackListener {
        fun onSuccess(responseJson: JSONObject)
        fun onFailure(errorMessage: String)
    }

    private val httpRequest = HttpRequest()

    fun sendTextRequest(
        length: Int,
        imageUrls: List<Uri>,
        type: String,
        originText: String,
        style: String,
        callbackListener: TextGenCallbackListener
    ) {
        val url = Contants.SERVER_ADDRESS + "/api/request/Text"
        // uri to string
        val stringList = mutableListOf<String>()
        for (uri in imageUrls) {
            val uriString = uri.toString()
            stringList.add(uriString)
        }
        val jsonBody = JSONObject().apply {
            put("length", length)
            put("imageUrls", JSONArray(stringList))
            put("type", type)
            put("originText", originText)
            put("style", style)
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
    fun formatUrlList(urlList: List<String>): String {
        var formattedString = ""
        for (url in urlList) {
            if (formattedString.isNotEmpty()) {
                formattedString += ","
            }
            formattedString += "$url"
        }
        return formattedString
    }

    fun sendSaveRequest(
        createdAt: String,
        updatedAt: String,
        originText: String,
        imageUrls: List<Uri>,
        labels: String,
        generatedText: String,
        type: String,
        callbackListener: TextGenCallbackListener
    ){
        val url = "http://10.0.2.2:8080/api/record/save" // Replace with your server address
        // uri to string
        val stringList = mutableListOf<String>()
        for (uri in imageUrls) {
            val uriString = uri.toString()
            stringList.add(uriString)
        }
        val imageString = formatUrlList(stringList);
        val jsonBody = JSONObject().apply {
            put("createdAt", createdAt)
            put("updatedAt", updatedAt)
            put("originText", originText)
            put("imageUrl", imageString)
            put("labels", labels)
            put("generatedText",generatedText)
            put("type", type)
        }
        val headerName = "satoken";
        val headerValue = AccountViewModel.token!!;
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

