package com.payconiq.qa.e2e

import com.payconiq.qa.data.CreateBookingData.Companion.ADDITIONAL_NEEDS
import com.payconiq.qa.data.CreateBookingData.Companion.CHECK_IN
import com.payconiq.qa.data.CreateBookingData.Companion.CHECK_OUT
import com.payconiq.qa.data.CreateBookingData.Companion.DEPOSIT_PAID
import com.payconiq.qa.data.CreateBookingData.Companion.TOTAL_PRICE
import com.payconiq.qa.data.CreateBookingData.Companion.UPDATED_ADDITIONAL_NEEDS
import com.payconiq.qa.data.CreateBookingData.Companion.UPDATED_DEPOSIT_PAID
import com.payconiq.qa.data.CreateBookingData.Companion.UPDATED_FIRST_NAME
import com.payconiq.qa.data.CreateBookingData.Companion.UPDATED_LAST_NAME
import com.payconiq.qa.data.CreateBookingData.Companion.UPDATED_TOTAL_PRICE
import com.payconiq.qa.extensions.preemptiveBasicAuth
import com.payconiq.qa.extensions.setHeaders
import com.payconiq.qa.extensions.setJSONBody
import com.payconiq.qa.extensions.validateErrorResponse
import com.payconiq.qa.model.CreateBooking.Companion.bookingID
import com.payconiq.qa.model.DeleteBooking.Companion.DELETE_BOOKING_RESOURCE_PATH
import com.payconiq.qa.model.DeleteBooking.Companion.validateResponseDeleteBooking
import com.payconiq.qa.model.OAuth
import com.payconiq.qa.model.PartialUpdateBooking.Companion.UPDATE_BOOKING_RESOURCE_PATH
import com.payconiq.qa.model.PartialUpdateBooking.Companion.accessToken
import com.payconiq.qa.model.PartialUpdateBooking.Companion.validateResponseUpdateBooking
import com.payconiq.qa.util.CommonConstants.Header.ACCEPT
import com.payconiq.qa.util.CommonConstants.Header.CONTENT_TYPE_HEADER
import com.payconiq.qa.util.CommonConstants.Header.COOKIE
import com.payconiq.qa.util.CommonConstants.TestTag.PIPELINE_1
import com.payconiq.qa.util.CommonConstants.TestTag.REGRESSION
import com.payconiq.qa.util.ConfigurationSpecification
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.ValueSource

/**
 * Test cases for Partial Update Booking
 * @author: Dilshan Fernando
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class PartialUpdateBookingTest : OAuth() {

    @BeforeAll
    fun init() {
        //get access token
        accessToken = getBasicAccessToken()

        /**
         * Injecting the value for bookingId from the CreateBookingTest
         */
        CreateBookingTest().run { `IDE-0001 - Verify user get HTTP 200 response when Create Booking with valid booking details`() }
    }

    @Test
    @Order(1)
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-3001 - Verify user get HTTP 415 response when Partial Update a Booking with invalid Content-Type`() {
        //set request headers
        val headers = mutableMapOf<String, String>()
        headers[CONTENT_TYPE_HEADER] = "text/plain"
        headers[COOKIE] = "token=${accessToken}"
        headers[ACCEPT] = "application/json"

        Given {
            setHeaders(headers.toMap())
            setJSONBody(
                """
                {
                    "firstname": "$UPDATED_FIRST_NAME",
                    "lastname": "$UPDATED_LAST_NAME"
                }
                """.trimIndent()
            )
        } When {
            put("${UPDATE_BOOKING_RESOURCE_PATH}/${bookingID}")
        } Then {
            validateErrorResponse(415, "Unsupported Media Type", "Please check the Content-Type")
        }
    }

    @Test
    @Order(2)
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-3002 - Verify user get HTTP 401 response when Partial Update a Booking without Cookie header`() {
        //set request headers
        val headers = mutableMapOf<String, String>()
        headers[CONTENT_TYPE_HEADER] = "application/json"
        headers[ACCEPT] = "application/json"

        Given {
            setHeaders(headers.toMap())
            setJSONBody(
                """
                {
                    "firstname": "$UPDATED_FIRST_NAME",
                    "lastname": "$UPDATED_LAST_NAME"
                }
                """.trimIndent()
            )
        } When {
            put("${UPDATE_BOOKING_RESOURCE_PATH}/${bookingID}")
        } Then {
            validateErrorResponse(401, "Unauthorized", "Please check the Cookie value")
        }
    }

    @Test
    @Order(3)
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-3003 - Verify user get HTTP 401 response when Partial Update a Booking with invalid Cookie value`() {
        //set request headers
        val headers = mutableMapOf<String, String>()
        headers[CONTENT_TYPE_HEADER] = "application/json"
        headers[COOKIE] = "token=invalid"
        headers[ACCEPT] = "application/json"

        Given {
            setHeaders(headers.toMap())
            setJSONBody(
                """
                {
                    "firstname": "$UPDATED_FIRST_NAME",
                    "lastname": "$UPDATED_LAST_NAME"
                }
                """.trimIndent()
            )
        } When {
            put("${UPDATE_BOOKING_RESOURCE_PATH}/${bookingID}")
        } Then {
            validateErrorResponse(401, "Unauthorized", "Please check the Cookie value")
        }
    }

    @Test
    @Order(4)
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-3004 - Verify user get HTTP 401 response when Partial Update a Booking with invalid token - Basic OAuth`() {
        //set request headers
        val headers = mutableMapOf<String, String>()
        headers[CONTENT_TYPE_HEADER] = "application/json"
        headers[ACCEPT] = "application/json"

        Given {
            setHeaders(headers.toMap())
            preemptiveBasicAuth("invalid", "invalid")
            setJSONBody(
                """
                {
                    "firstname": "$UPDATED_FIRST_NAME",
                    "lastname": "$UPDATED_LAST_NAME"
                }
                """.trimIndent()
            )
        } When {
            put("${UPDATE_BOOKING_RESOURCE_PATH}/${bookingID}")
        } Then {
            validateErrorResponse(401, "Unauthorized", "Please check the Cookie value")
        }
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(
        strings = [
            "tes@$#%t",
            "454.45"
        ]
    )
    @Order(5)
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-3005 - Verify user get HTTP 400 response when Partial Update a Booking - Invalid bookingID`(bookingIDs: String?) {
        //set request headers
        val headers = mutableMapOf<String, String>()
        headers[CONTENT_TYPE_HEADER] = "application/json"
        headers[ACCEPT] = "application/json"

        Given {
            setHeaders(headers.toMap())
            preemptiveBasicAuth(
                ConfigurationSpecification.getUserName(),
                ConfigurationSpecification.getPassword()
            )
            setJSONBody(
                """
                {
                    "firstname": "$UPDATED_FIRST_NAME",
                    "lastname": "$UPDATED_LAST_NAME"
                }
                """.trimIndent()
            )
        } When {
            put("${UPDATE_BOOKING_RESOURCE_PATH}/$bookingIDs")
        } Then {
            validateErrorResponse(400, "Bad Request", "Please check the Booking details")
        }
    }

    @Test
    @Order(6)
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-3006 - Verify user get HTTP 404 response when Partial Update a Booking - bookingID is not found`() {
        //set request headers
        val headers = mutableMapOf<String, String>()
        headers[CONTENT_TYPE_HEADER] = "application/json"
        headers[ACCEPT] = "application/json"

        Given {
            setHeaders(headers.toMap())
            preemptiveBasicAuth(
                ConfigurationSpecification.getUserName(),
                ConfigurationSpecification.getPassword()
            )
            setJSONBody(
                """
                {
                    "firstname": "$UPDATED_FIRST_NAME",
                    "lastname": "$UPDATED_LAST_NAME"
                }
                """.trimIndent()
            )
        } When {
            put("${UPDATE_BOOKING_RESOURCE_PATH}/20381")
        } Then {
            validateErrorResponse(404, "Record Not Found", "Please check the BookingID")
        }
    }

    @Test
    @Order(7)
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-3007 - Verify user get HTTP 400 response when Partial Update a Booking with invalid booking details`() {
        //set request headers
        val headers = mutableMapOf<String, String>()
        headers[CONTENT_TYPE_HEADER] = "application/json"
        headers[COOKIE] = "token=${accessToken}"
        headers[ACCEPT] = "application/json"

        Given {
            setHeaders(headers.toMap())
            setJSONBody(
                """
                {
                    "price": 125.64,
                    "checkin":"2018-01-01"
                }
                """.trimIndent()
            )
        } When {
            put("${UPDATE_BOOKING_RESOURCE_PATH}/${bookingID}")
        } Then {
            validateErrorResponse(400, "Bad Request", "Please check the Booking details")
        }
    }

    @Test
    @Order(8)
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-3008 - Verify user get HTTP 405 response when Partial Update a Booking - Invalid HTTP Method`() {
        //set request headers
        val headers = mutableMapOf<String, String>()
        headers[CONTENT_TYPE_HEADER] = "application/json"
        headers[ACCEPT] = "application/json"

        Given {
            setHeaders(headers.toMap())
            preemptiveBasicAuth(
                ConfigurationSpecification.getUserName(),
                ConfigurationSpecification.getPassword()
            )
            setJSONBody(
                """
                {
                    "firstname": "$UPDATED_FIRST_NAME",
                    "lastname": "$UPDATED_LAST_NAME"
                }
                """.trimIndent()
            )
        } When {
            post("${UPDATE_BOOKING_RESOURCE_PATH}/${bookingID}")
        } Then {
            validateErrorResponse(405, "Method Not Allowed", "Please check the HTTP Method")
        }
    }

    @Test
    @Order(9)
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-3008 - Verify user get HTTP 200 response when Partial Update a Booking - Basic OAuth`() {
        //set request headers
        val headers = mutableMapOf<String, String>()
        headers[CONTENT_TYPE_HEADER] = "application/json"
        headers[ACCEPT] = "application/json"

        Given {
            setHeaders(headers.toMap())
            preemptiveBasicAuth(
                ConfigurationSpecification.getUserName(),
                ConfigurationSpecification.getPassword()
            )
            setJSONBody(
                """
                {
                    "firstname": "$UPDATED_FIRST_NAME",
                    "lastname": "$UPDATED_LAST_NAME"
                }
                """.trimIndent()
            )
        } When {
            put("${UPDATE_BOOKING_RESOURCE_PATH}/${bookingID}")
        } Then {
            validateResponseUpdateBooking(
                UPDATED_FIRST_NAME,
                UPDATED_LAST_NAME,
                TOTAL_PRICE,
                DEPOSIT_PAID,
                CHECK_IN,
                CHECK_OUT,
                ADDITIONAL_NEEDS
            )
        }
    }

    @Test
    @Order(10)
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-3009 - Verify user get HTTP 200 response when Partial Update a Booking - Cookie value`() {
        //set request headers
        val headers = mutableMapOf<String, String>()
        headers[CONTENT_TYPE_HEADER] = "application/json"
        headers[COOKIE] = "token=${accessToken}"
        headers[ACCEPT] = "application/json"

        Given {
            setHeaders(headers.toMap())
            setJSONBody(
                """
                {
                    "totalprice": $UPDATED_TOTAL_PRICE,
                    "depositpaid": $UPDATED_DEPOSIT_PAID
                }
                """.trimIndent()
            )
        } When {
            put("${UPDATE_BOOKING_RESOURCE_PATH}/${bookingID}")
        } Then {
            validateResponseUpdateBooking(
                UPDATED_FIRST_NAME,
                UPDATED_LAST_NAME,
                UPDATED_TOTAL_PRICE,
                UPDATED_DEPOSIT_PAID,
                CHECK_IN,
                CHECK_OUT,
                ADDITIONAL_NEEDS
            )
        }
    }

    @Test
    @Order(11)
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-3010 - Verify user get HTTP 200 response when Partial Update a Booking - URL Encoded`() {
        //set request headers
        val headers = mutableMapOf<String, String>()
        headers[ACCEPT] = "application/x-www-form-urlencoded"

        Given {
            setHeaders(headers.toMap())
            preemptiveBasicAuth(
                ConfigurationSpecification.getUserName(),
                ConfigurationSpecification.getPassword()
            )
            contentType(ContentType.URLENC)
            formParam(ADDITIONAL_NEEDS, UPDATED_ADDITIONAL_NEEDS)
        } When {
            put("${UPDATE_BOOKING_RESOURCE_PATH}/${bookingID}")
        } Then {
            validateResponseUpdateBooking(
                UPDATED_FIRST_NAME,
                UPDATED_LAST_NAME,
                UPDATED_TOTAL_PRICE,
                UPDATED_DEPOSIT_PAID,
                CHECK_IN,
                CHECK_OUT,
                UPDATED_ADDITIONAL_NEEDS
            )
        }
    }

    @AfterAll
    fun clearTestData() {
        //delete above created booking
        val headers = mutableMapOf<String, String>()
        headers[CONTENT_TYPE_HEADER] = "application/json"
        headers[COOKIE] = "token=${accessToken}"

        Given {
            setHeaders(headers.toMap())
        } When {
            delete("${DELETE_BOOKING_RESOURCE_PATH}/$bookingID")
        } Then {
            validateResponseDeleteBooking()
        }
    }
}