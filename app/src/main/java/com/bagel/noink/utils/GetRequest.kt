package com.bagel.noink.utils

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class GetRequest {
    fun getHelloMessage() {
        // Get通信函数
        val client = OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build()
        val request =
            Request.Builder().url("http://172.29.4.128:8080/hello").method("GET", null).build()
        val call = client.newCall(request)

        call.enqueue(object : Callback {
            //请求失败调用
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to connect to the backend")
            }

            //请求结束调用（意味着与服务器的通信成功）
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    println(response.body?.string())
                }
            }
        })


    }

    //get同步请求
    fun getSync() {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("http://172.29.4.128:8080/hello")
            .build()

        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                println(responseBody)
            } else {
                println("Request failed: ${response.code}")
            }
        }
    }


}