package com.bagel.noink.utils
import android.net.Uri
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
        val url = "http://localhost:8080/api/request/Text" // Replace with your server address
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
}

