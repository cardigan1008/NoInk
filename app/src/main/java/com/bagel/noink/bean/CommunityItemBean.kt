package com.bagel.noink.bean

import android.net.Uri
data class CommunityItemBean(
    var avatar: Uri,
    var imageUrls: List<Uri>?,
    var username: String,
    var content: String,
    var likeNumber: Int,
    var commentNumber: Int
)
