package com.bagel.noink.bean
import android.net.Uri
data class RecordCardBean(
    var date: String,
    var title: String,
    var photo: Uri,
    var content: String
)
