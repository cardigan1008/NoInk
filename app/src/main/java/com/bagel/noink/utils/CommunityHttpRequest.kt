package com.bagel.noink.utils

import org.json.JSONObject

class CommunityHttpRequest {
    interface CommunityCallbackListener {
        fun onSuccess(responseJson: JSONObject)
        fun onFailure(errorMessage: String)
    }
    private val httpRequest = HttpRequest()
    fun getCommunityContent(){

    }
}