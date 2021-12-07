package com.payconiq.qa.extensions

import io.restassured.specification.RequestSpecification

/**
 * Keep all common methods to a request
 * @author: Dilshan Fernando
 * @since: 06/12/2021
 */


/**
 * Set Query Parameters to request
 */
fun RequestSpecification.setQueryParameters(keyValues: Map<String, String?>): RequestSpecification {
    for ((k, v) in keyValues) {
        queryParam(k, v)
    }
    return this
}

/**
 * Set headers to request
 */
fun RequestSpecification.setHeaders(keyValues: Map<String, String?>): RequestSpecification {
    for ((k, v) in keyValues) {
        header(k, v)
    }
    return this
}

/**
 * Including a content json type body in the request
 */
fun RequestSpecification.setJSONBody(jsonString: String): RequestSpecification =
    body(jsonString)
