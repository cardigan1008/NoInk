package com.bagel.noink.ui.home

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.bagel.noink.bean.TextGenInfoBean
import org.json.JSONObject

class TextGenViewModel {
    companion object {
        // 用户信息
        var textGenInfo: TextGenInfoBean? = TextGenInfoBean(
            null,
            null,
            null,
            null,
        )
        fun updateInfoUrls(imageUrl: List<Uri>){
            textGenInfo?.imageUrls = imageUrl
        }
        fun updateOriginText(originText: String){
            textGenInfo?.originText = originText
        }
        fun updateStyle(style: String){
            textGenInfo?.style = style
        }
        fun updateType(type: String){
            textGenInfo?.type = type
        }

        fun getInfoUrls(): List<Uri>? {
            return textGenInfo?.imageUrls
        }
        fun getOriginText(): String? {
            return textGenInfo?.originText
        }
        fun getStyle(): String? {
            return textGenInfo?.style
        }
        fun getType(): String? {
            return textGenInfo?.type
        }
    }
}