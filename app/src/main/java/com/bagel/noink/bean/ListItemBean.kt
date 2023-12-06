package com.bagel.noink.bean

import android.net.Uri

data class ListItemBean (
    val id: Int,
    val title: String?,
    val text: String?,
    val coverUri: Uri,
    val imagesUri: List<Uri>
)