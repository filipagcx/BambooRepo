package com.deliveroo.orderapp.base.service.place.api

import timber.log.Timber.d
import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.Locale
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class ApiSecretGenerator @Inject constructor() {

    private val ALGO = "HmacSHA1"

    fun generate(params: Map<String, String>, envString: String): String {
        d("RPS: got params = $params")

        // sort parameters by key, then create key=value pairs
        val sortedParams = params
                .toSortedMap()
                .map { (key, value) -> "$key=$value" }

        d("RPS: sorted params = $sortedParams")

        // concatenate all key-value pairs
        val concatenatedParams = sortedParams.joinToString(separator = "")

        d("RPS: concatenated params = $concatenatedParams")

        // create a HMAC-SHA1 digest
        val digest = HmacSha1(concatenatedParams, envString).toLowerCase(Locale.ROOT)

        d("RPS: resulted digest = $digest")

        return digest
    }

    // See https://developer.android.com/reference/javax/crypto/Mac.html
    @Throws(UnsupportedEncodingException::class, NoSuchAlgorithmException::class, InvalidKeyException::class)
    private fun HmacSha1(value: String, key: String): String {
        val secret = SecretKeySpec(key.toByteArray(), ALGO)
        val mac = Mac.getInstance(ALGO)
        mac.init(secret)
        val bytes = mac.doFinal(value.toByteArray())
        return bytesToHex(bytes)
    }

    private fun bytesToHex(messageDigest: ByteArray): String =
            messageDigest.fold(StringBuilder()) { acc, b ->
                var h = Integer.toHexString(0xFF and b.toInt())
                while (h.length < 2) h = "0$h"
                acc.append(h)
            }.toString()
}