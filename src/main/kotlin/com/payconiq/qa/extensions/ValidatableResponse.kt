package com.payconiq.qa.extensions

import io.restassured.response.ValidatableResponse
import org.hamcrest.CoreMatchers

/**
 * Keep all common methods to a response
 * @author: Dilshan Fernando
 * @since: 06/12/2021
 */

/**
 * Verify the error response headers and body
 * Error response should follow RFC standard:
 * RFC-7807 - Problem Details for HTTP APIs: https://datatracker.ietf.org/doc/html/rfc7807
 * Responses to POST, PUT and DELETE methods are not cacheable, unless the response includes appropriate Cache-Control or Expires header fields.
 * For this exercise, I have assumed, Content-Type type to be NOT text/plain;
 */
fun ValidatableResponse.validateErrorResponse(
    httpStatusCode: Int,
    error: String,
    errorDescription: String
): ValidatableResponse = statusCode(httpStatusCode)
    .body("error", CoreMatchers.`is`(error))
    .body("error_description", CoreMatchers.`is`(errorDescription))
    .headers(
        mapOf(
            "Connection" to "keep-alive",
            "content-length" to CoreMatchers.notNullValue(),
            "Content-Type" to "application/json; charset=utf-8",
            "Etag" to CoreMatchers.notNullValue(),
        )
    )