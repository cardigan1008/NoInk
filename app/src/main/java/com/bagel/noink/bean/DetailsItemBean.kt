package com.bagel.noink.bean

import java.net.URI

data class DetailsItemBean(
    val pictures: ArrayList<URI>,
    val text: String,
)