package com.deliveroo.orderapp.base.service.place.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class ApiSecretGeneratorTest {

    private lateinit var generator: ApiSecretGenerator

    @Before
    fun setup() {
        generator = ApiSecretGenerator()
    }

    @Test
    fun generate() {
        val secret = generator.generate(mapOf(
                "input" to "moye%20close&baz",
                "location" to "51.3417220977031,0.050099790096283",
                "key" to "AIzaSyBKimK0MwC9St_1Zloclm3ljmuU61WsrLI",
                "radius" to "1000"), "hello1234")

        // see CCNR-244 for expected result
        assertThat(secret).isEqualTo("66549ef1f36e2002ac9d95b2a4c6796a3462ddef")
    }
}