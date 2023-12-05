package com.bagel.noink.bean

data class UserInfoBean(
    var id : Int,
    var username: String,
    var password: String,
    var gender: Boolean,
    var age: Int,
    var wechatId: String,
    var birthday: String
)
