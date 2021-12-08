package com.payconiq.qa.e2e

import com.payconiq.qa.extensions.preemptiveBasicAuth
import com.payconiq.qa.extensions.setHeaders
import com.payconiq.qa.extensions.validateErrorResponse
import com.payconiq.qa.model.CreateBooking.Companion.bookingID
import com.payconiq.qa.model.DeleteBooking.Companion.DELETE_BOOKING_RESOURCE_PATH
import com.payconiq.qa.model.DeleteBooking.Companion.accessToken
import com.payconiq.qa.model.DeleteBooking.Companion.validateResponseDeleteBooking
import com.payconiq.qa.model.OAuth
import com.payconiq.qa.util.CommonConstants.Header.CONTENT_TYPE_HEADER
import com.payconiq.qa.util.CommonConstants.Header.COOKIE
import com.payconiq.qa.util.CommonConstants.TestTag.PIPELINE_1
import com.payconiq.qa.util.CommonConstants.TestTag.REGRESSION
import com.payconiq.qa.util.ConfigurationSpecification
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.ValueSource

/**
 * Test cases for Delete Booking
 * @author: Dilshan Fernando
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class DeleteBookingTest : OAuth() {

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
    fun `IDE-2001 - Verify user get HTTP 415 response when Delete Booking with invalid Content-Type`() {
        //set request headers
        val headers = mutableMapOf<String, String>()
        headers[CONTENT_TYPE_HEADER] = "text/plain"
        headers[COOKIE] = "token=$accessToken"

        Given {
            setHeaders(headers.toMap())
        } When {
            delete("$DELETE_BOOKING_RESOURCE_PATH/$bookingID")
        } Then {
            validateErrorResponse(415, "Unsupported Media Type", "Please check the Content-Type")
        }
    }

    @Test
    @Order(7)
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-2009 - Verify user get HTTP 401 response when Delete Booking without Cookie header`() {
        //set request headers
        val headers = mutableMapOf<String, String>()
        headers[CONTENT_TYPE_HEADER] = "application/json"

        Given {
            setHeaders(headers.toMap())
        } When {
            delete("$DELETE_BOOKING_RESOURCE_PATH/$bookingID")
        } Then {
            validateErrorResponse(401, "Unauthorized", "Please check the Cookie value")
        }
    }

    @Test
    @Order(2)
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-2002 - Verify user get HTTP 401 response when Delete Booking with invalid Cookie value`() {
        //set request headers
        val headers = mutableMapOf<String, String>()
        headers[CONTENT_TYPE_HEADER] = "application/json"
        headers[COOKIE] = "token=invalid"

        Given {
            setHeaders(headers.toMap())
        } When {
            delete("$DELETE_BOOKING_RESOURCE_PATH/$bookingID")
        } Then {
            validateErrorResponse(401, "Unauthorized", "Please check the Cookie value")
        }
    }

    @Test
    @Order(8)
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-2003 - Verify user get HTTP 200 response when Delete Booking - Basic OAuth`() {
        //set request headers
        val headers = mutableMapOf<String, String>()
        headers[CONTENT_TYPE_HEADER] = "application/json"

        Given {
            setHeaders(headers.toMap())
            preemptiveBasicAuth(
                ConfigurationSpecification.getUserName(),
                ConfigurationSpecification.getPassword()
            )
        } When {
            delete("$DELETE_BOOKING_RESOURCE_PATH/$bookingID")
        } Then {
            validateResponseDeleteBooking()
        }
    }

    @Test
    @Order(9)
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-2004 - Verify user get HTTP 200 response when Delete Booking - Cookie`() {
        //delete above created booking
        val headers = mutableMapOf<String, String>()
        headers[CONTENT_TYPE_HEADER] = "application/json"
        headers[COOKIE] = "token=$accessToken"

        /**
         * Injecting the value for bookingId from the CreateBookingTest
         */
        CreateBookingTest().run { `IDE-0001 - Verify user get HTTP 200 response when Create Booking with valid booking details`() }

        Given {
            setHeaders(headers.toMap())
        } When {
            delete("$DELETE_BOOKING_RESOURCE_PATH/$bookingID")
        } Then {
            validateResponseDeleteBooking()
        }
    }

    @Test
    @Order(3)
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-2005 - Verify user get HTTP 401 response when Delete Booking with invalid token - Basic OAuth`() {
        //set request headers
        val headers = mutableMapOf<String, String>()
        headers[CONTENT_TYPE_HEADER] = "application/json"

        Given {
            setHeaders(headers.toMap())
            preemptiveBasicAuth("invalid", "invalid")
        } When {
            delete("$DELETE_BOOKING_RESOURCE_PATH/$bookingID")
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
    @Order(4)
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-2006 - Verify user get HTTP 400 response when Delete Booking - Invalid bookingID`(bookingIDs: String?) {
        //set request headers
        val headers = mutableMapOf<String, String>()
        headers[CONTENT_TYPE_HEADER] = "application/json"

        Given {
            setHeaders(headers.toMap())
            preemptiveBasicAuth(
                ConfigurationSpecification.getUserName(),
                ConfigurationSpecification.getPassword()
            )
        } When {
            delete("$DELETE_BOOKING_RESOURCE_PATH/$bookingIDs")
        } Then {
            validateErrorResponse(400, "Bad Request", "Please check the Booking details")
        }
    }

    @Test
    @Order(5)
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-2007 - Verify user get HTTP 404 response when Delete Booking - bookingID is not found`() {
        //set request headers
        val headers = mutableMapOf<String, String>()
        headers[CONTENT_TYPE_HEADER] = "application/json"

        Given {
            setHeaders(headers.toMap())
            preemptiveBasicAuth(
                ConfigurationSpecification.getUserName(),
                ConfigurationSpecification.getPassword()
            )
        } When {
            delete("$DELETE_BOOKING_RESOURCE_PATH/45926")
        } Then {
            validateErrorResponse(404, "Record Not Found", "Please check the BookingID")
        }
    }

    @Test
    @Order(6)
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-2008 - Verify user get HTTP 405 response when Delete Booking - Invalid HTTP Method`() {
        //set request headers
        val headers = mutableMapOf<String, String>()
        headers[CONTENT_TYPE_HEADER] = "application/json"

        Given {
            setHeaders(headers.toMap())
            preemptiveBasicAuth(
                ConfigurationSpecification.getUserName(),
                ConfigurationSpecification.getPassword()
            )
        } When {
            post("$DELETE_BOOKING_RESOURCE_PATH/$bookingID")
        } Then {
            validateErrorResponse(405, "Method Not Allowed", "Please check the HTTP Method")
        }
    }

}