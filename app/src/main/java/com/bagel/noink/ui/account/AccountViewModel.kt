package com.bagel.noink.ui.account

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bagel.noink.bean.UserInfoBean

class AccountViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    val text: LiveData<String> = _text

    companion object {
        var token : String ?= null

        // 用户信息
        var userInfo : UserInfoBean ?= null
    }

}