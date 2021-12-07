package com.payconiq.qa.extensions

import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponse
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers

/**
 * Keep all common methods to a response
 * @author: Dilshan Fernando
 * @since: 06/12/2021
 */

/**
 * Verify the successful response headers
 */
fun ValidatableResponse.validateSuccessResponseHeaders(): ValidatableResponse =
    statusCode(HttpStatus.SC_OK)
        .headers(
            mapOf(
                "Cache-Control" to "max-age=43200",
                "Pragma" to "no-cache",
                "Content-Type" to CoreMatchers.containsString(ContentType.JSON.toString())
            )
        )
