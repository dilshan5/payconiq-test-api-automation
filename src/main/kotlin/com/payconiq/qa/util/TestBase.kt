package com.payconiq.qa.util

import io.restassured.RestAssured
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll

/**
 * Base class for the Test classes
 * @author: Dilshan Fernando
 * @since: 06/12/2021
 */
abstract class TestBase {

    companion object {

        /**
         * set project configurations
         */
        @BeforeAll
        @JvmStatic
        fun setup() {
            RestAssured.baseURI = "https://${ConfigurationSpecification.getHost()}"
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        }

        /**
         * Include the cleanup code
         */
        @AfterAll
        @JvmStatic
        fun cleanUp() {

        }

    }
}