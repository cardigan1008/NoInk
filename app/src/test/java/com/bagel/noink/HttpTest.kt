package com.bagel.noink
import com.bagel.noink.utils.GetRequest
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