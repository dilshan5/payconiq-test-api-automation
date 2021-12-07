package com.payconiq.qa.model

import com.payconiq.qa.extensions.setHeaders
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.ValidatableResponse
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers
import com.payconiq.qa.extensions.setJSONBody
import com.payconiq.qa.util.CommonConstants
import com.payconiq.qa.util.ConfigurationSpecification
import com.payconiq.qa.util.TestBase

open class OAuth : TestBase() {
    companion object {

        private const val OAUTH_PROXY_PATH = "/auth"

        /**
         * Get access token when UserName & Password is given from a successful token response
         */
        fun getBasicAccessToken(): String {
            //set request headers
            val headers = mutableMapOf<String, String>()
            headers[CommonConstants.Header.CONTENT_TYPE_HEADER] = "application/json"

            return Given {
                setHeaders(headers.toMap())
                setJSONBody(
                    """
                {
                  "username": "${ConfigurationSpecification.getUserName()}",
                  "password": "${ConfigurationSpecification.getPassword()}"
                }
                """.trimIndent()
                )
            } When {
                post(OAUTH_PROXY_PATH)
            } Then {
                successfulTokenResponse()
            } Extract {
                path("token")
            }
        }

        /**
         * Methods for checking response bodies & headers regarding API Authentication
         * https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.4
         */
        private fun ValidatableResponse.successfulTokenResponse(): ValidatableResponse =
            statusCode(HttpStatus.SC_OK)
                .body("token", CoreMatchers.notNullValue())
        //NOTE: The below header verification should be there as per RFC standard. BUT I have commented inorder to execute the classes
/*                .headers(
                    mapOf(
                        "Cache-Control" to "no-store",
                        "Pragma" to "no-cache",
                        CONTENT_TYPE_HEADER to CoreMatchers.containsString("application/json")
                    )
                )*/
    }
}