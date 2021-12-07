package com.payconiq.qa.e2e

import com.payconiq.qa.data.CreateBookingData
import com.payconiq.qa.extensions.setHeaders
import com.payconiq.qa.extensions.setJSONBody
import com.payconiq.qa.model.CreateBooking.Companion.CREATE_BOOKING_RESOURCE_PATH
import com.payconiq.qa.model.CreateBooking.Companion.accessToken
import com.payconiq.qa.model.CreateBooking.Companion.bookingID
import com.payconiq.qa.model.CreateBooking.Companion.validateResponseCreateBooking
import com.payconiq.qa.model.DeleteBooking.Companion.DELETE_BOOKING_RESOURCE_PATH
import com.payconiq.qa.model.DeleteBooking.Companion.validateResponseDeleteBooking
import com.payconiq.qa.model.OAuth
import com.payconiq.qa.util.CommonConstants.TestTag.PIPELINE_1
import com.payconiq.qa.util.CommonConstants.TestTag.REGRESSION
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

/**
 * Test cases for Create Booking
 * @author: Dilshan Fernando
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateBookingTest : OAuth() {

    @BeforeAll
    fun init() {
        //get access token
        accessToken = getBasicAccessToken()
    }

    @ParameterizedTest
    @MethodSource("com.payconiq.qa.data.CreateBookingData#validBookingData")
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-0001 - Verify the success response of Create Booking`(bookingData: Map<String, Any>) {
        //get values for Booking
        var firstName: String = bookingData[CreateBookingData.Companion.Keys.FIRST_NAME].toString()
        var lastName: String = bookingData[CreateBookingData.Companion.Keys.LAST_NAME].toString()
        var totalPrice: Int = bookingData[CreateBookingData.Companion.Keys.TOTAL_PRICE] as Int
        var depositPaid: Boolean = bookingData[CreateBookingData.Companion.Keys.DEPOSIT_PAID] as Boolean
        var checkIN: String = bookingData[CreateBookingData.Companion.Keys.CHECK_IN].toString()
        var checkOUT: String = bookingData[CreateBookingData.Companion.Keys.CHECK_OUT].toString()
        var additionalNeeds: String = bookingData[CreateBookingData.Companion.Keys.ADDITIONAL_NEEDS].toString()

        //store booking ID and delete the booking after the test has completed
        bookingID = Given {
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
            validateResponseCreateBooking(
                firstName,
                lastName,
                totalPrice,
                depositPaid,
                checkIN,
                checkOUT,
                additionalNeeds
            )
        } Extract {
            path("bookingid")
        }
    }

    @AfterAll
    fun clearTestData() {
        val headers = mutableMapOf<String, String>()
        headers["Content-Type"] = "application/json"
        headers["Cookie"] = "token=$accessToken"

        Given {
            setHeaders(headers.toMap())
        } When {
            delete("$DELETE_BOOKING_RESOURCE_PATH/$bookingID")
        } Then {
            validateResponseDeleteBooking()
        }
    }

}