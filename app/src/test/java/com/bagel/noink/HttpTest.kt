package com.bagel.noink
import org.junit.Test

import org.junit.Assert.*

class HttpTest {
    @Test
    fun helloTest() {
        val getRequestHelper = GetRequest()
        getRequestHelper.getHelloMessage();
        getRequestHelper.getSync();
    }
}