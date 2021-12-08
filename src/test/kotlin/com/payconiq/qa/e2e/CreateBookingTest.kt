package com.payconiq.qa.e2e

import com.payconiq.qa.data.CreateBookingData
import com.payconiq.qa.data.CreateBookingData.Companion.ADDITIONAL_NEEDS
import com.payconiq.qa.data.CreateBookingData.Companion.CHECK_IN
import com.payconiq.qa.data.CreateBookingData.Companion.CHECK_OUT
import com.payconiq.qa.data.CreateBookingData.Companion.DEPOSIT_PAID
import com.payconiq.qa.data.CreateBookingData.Companion.FIRST_NAME
import com.payconiq.qa.data.CreateBookingData.Companion.LAST_NAME
import com.payconiq.qa.data.CreateBookingData.Companion.TOTAL_PRICE
import com.payconiq.qa.data.CreateBookingData.Companion.generateValidBookingDetails
import com.payconiq.qa.extensions.setHeaders
import com.payconiq.qa.extensions.setJSONBody
import com.payconiq.qa.extensions.validateErrorResponse
import com.payconiq.qa.model.CreateBooking.Companion.CREATE_BOOKING_RESOURCE_PATH
import com.payconiq.qa.model.CreateBooking.Companion.accessToken
import com.payconiq.qa.model.CreateBooking.Companion.bookingID
import com.payconiq.qa.model.CreateBooking.Companion.validateResponseCreateBooking
import com.payconiq.qa.model.DeleteBooking.Companion.DELETE_BOOKING_RESOURCE_PATH
import com.payconiq.qa.model.DeleteBooking.Companion.validateResponseDeleteBooking
import com.payconiq.qa.model.OAuth
import com.payconiq.qa.util.CommonConstants.Header.CONTENT_TYPE_HEADER
import com.payconiq.qa.util.CommonConstants.Header.COOKIE
import com.payconiq.qa.util.CommonConstants.TestTag.PIPELINE_1
import com.payconiq.qa.util.CommonConstants.TestTag.REGRESSION
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EmptySource
import org.junit.jupiter.params.provider.MethodSource

/**
 * Test cases for Create Booking
 * @author: Dilshan Fernando
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CreateBookingTest : OAuth() {

    @BeforeAll
    fun init() {
        //get access token
        accessToken = getBasicAccessToken()
    }

    @Test
    @Order(1)
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-0001 - Verify user get HTTP 200 response when Create Booking with valid booking details`() {
        //set request headers
        val headers = mutableMapOf<String, String>()
        headers[CONTENT_TYPE_HEADER] = "application/json"

        //store booking ID and delete the booking after the test has completed
        bookingID = Given {
            setHeaders(headers.toMap())
            setJSONBody(generateValidBookingDetails().trimIndent())
        } When {
            post(CREATE_BOOKING_RESOURCE_PATH)
        } Then {
            validateResponseCreateBooking(
                FIRST_NAME,
                LAST_NAME,
                TOTAL_PRICE,
                DEPOSIT_PAID,
                CHECK_IN,
                CHECK_OUT,
                ADDITIONAL_NEEDS
            )
        } Extract {
            path("bookingid")
        }
    }

    @Test
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-0002 - Verify user get HTTP 415 response when Create Booking with invalid Content-Type`() {
        //set request headers
        val headers = mutableMapOf<String, String>()
        headers[CONTENT_TYPE_HEADER] = "text/plain"

        Given {
            setHeaders(headers.toMap())
            setJSONBody(generateValidBookingDetails().trimIndent())
        } When {
            post(CREATE_BOOKING_RESOURCE_PATH)
        } Then {
            validateErrorResponse(415, "Unsupported Media Type", "Please check the Content-Type")
        }
    }

    @Test
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-0003 - Verify user get HTTP 404 response when Create Booking with invalid endpoint`() {
        //set request headers
        val headers = mutableMapOf<String, String>()
        headers[CONTENT_TYPE_HEADER] = "application/json"

        Given {
            setHeaders(headers.toMap())
            setJSONBody(generateValidBookingDetails().trimIndent())
        } When {
            post("/bookings")
        } Then {
            validateErrorResponse(404, "Not Found", "Resource Not Found")
        }
    }

    @Test
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-0004 - Verify user get HTTP 405 response when Create Booking with invalid HTTP method`() {
        //set request headers
        val headers = mutableMapOf<String, String>()
        headers[CONTENT_TYPE_HEADER] = "application/json"

        Given {
            setHeaders(headers.toMap())
            setJSONBody(generateValidBookingDetails().trimIndent())
        } When {
            patch(CREATE_BOOKING_RESOURCE_PATH)
        } Then {
            validateErrorResponse(405, "Method Not Allowed", "Please check the HTTP Method")
        }
    }

    @ParameterizedTest
    @EmptySource
    @MethodSource("com.payconiq.qa.data.CreateBookingData#invalidBookingData")
    @Tags(Tag(REGRESSION))
    fun `IDE-0005 - Verify user get HTTP 400 response when Create Booking with invalid booking details`(bookingData: Map<String, Any?>) {
        //get values for Booking
        val firstName: Any? = bookingData[CreateBookingData.Companion.Keys.FIRST_NAME]
        val lastName: Any? = bookingData[CreateBookingData.Companion.Keys.LAST_NAME]
        val totalPrice: Any? = bookingData[CreateBookingData.Companion.Keys.TOTAL_PRICE]
        val depositPaid: Any? = bookingData[CreateBookingData.Companion.Keys.DEPOSIT_PAID]
        val checkIN: Any? = bookingData[CreateBookingData.Companion.Keys.CHECK_IN]
        val checkOUT: Any? = bookingData[CreateBookingData.Companion.Keys.CHECK_OUT]
        val additionalNeeds: Any? = bookingData[CreateBookingData.Companion.Keys.ADDITIONAL_NEEDS]

        Given {
            setJSONBody(
                """
                {
                  "firstname": "$firstName",
                  "lastname": "$lastName", 
                  "totalprice": $totalPrice,
                  "depositpaid": $depositPaid,
                  "bookingdates": {
                       "checkin": "$checkIN",
                       "checkout": "$checkOUT"
                  
                  },
                  "additionalneeds": "$additionalNeeds"
                }
                """.trimIndent()
            )
        } When {
            post(CREATE_BOOKING_RESOURCE_PATH)
        } Then {
            validateErrorResponse(400, "Invalid Booking Request", "Please check the Booking details")
        }
    }


    @AfterAll
    fun clearTestData() {
        //delete above created booking
        val headers = mutableMapOf<String, String>()
        headers[CONTENT_TYPE_HEADER] = "application/json"
        headers[COOKIE] = "token=$accessToken"

        Given {
            setHeaders(headers.toMap())
        } When {
            delete("$DELETE_BOOKING_RESOURCE_PATH/$bookingID")
        } Then {
            validateResponseDeleteBooking()
        }
    }

}