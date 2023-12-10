package com.bagel.noink.bean

import android.net.Uri
import java.util.Date

data class ListItemBean (
    val id: Int,
    val title: String?,
    val text: String?,
    val coverUri: Uri,
    val imagesUri: List<Uri>,
    val createDate: Date
)