package com.payconiq.qa.e2e

import com.payconiq.qa.data.CreateBookingData.Companion.CHECK_IN
import com.payconiq.qa.data.CreateBookingData.Companion.CHECK_OUT
import com.payconiq.qa.data.CreateBookingData.Companion.FIRST_NAME
import com.payconiq.qa.data.CreateBookingData.Companion.Keys.KEY_CHECK_IN
import com.payconiq.qa.data.CreateBookingData.Companion.Keys.KEY_CHECK_OUT
import com.payconiq.qa.data.CreateBookingData.Companion.Keys.KEY_FIRST_NAME
import com.payconiq.qa.data.CreateBookingData.Companion.Keys.KEY_LAST_NAME
import com.payconiq.qa.data.CreateBookingData.Companion.LAST_NAME
import com.payconiq.qa.extensions.*
import com.payconiq.qa.model.CreateBooking.Companion.bookingID
import com.payconiq.qa.model.GetBookingIds
import com.payconiq.qa.util.CommonConstants.TestTag.PIPELINE_1
import com.payconiq.qa.util.CommonConstants.TestTag.REGRESSION
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetBookingIdsTest : GetBookingIds() {

    @BeforeAll
    fun init() {
        /**
         * Injecting the value for bookingId from the CreateBookingTest
         */
        CreateBookingTest().run { `IDE-0001 - Verify user get HTTP 200 response when Create Booking with valid booking details`() }
    }

    @Test
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-4001 - Verify user get HTTP 200 response when Get Booking details- All IDs`() {
        When {
            get(GET_BOOKING_RESOURCE_PATH)
        } Then {
            validateGetBookings(bookingID)
        }
    }

    @Test
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-4002 - Verify user get HTTP 405 response when Get ALl Booking details- Invalid HTTP Method`() {
        When {
            delete(GET_BOOKING_RESOURCE_PATH)
        } Then {
            validateErrorResponse(405, "Method Not Allowed", "Please check the HTTP Method")
        }
    }

    @Test
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-4003 - Verify user get HTTP 200 response when Get Booking details- Filter By Name`() {
        val queryParameters = mutableMapOf<String, String>()
        queryParameters[KEY_FIRST_NAME] = FIRST_NAME
        queryParameters[KEY_LAST_NAME] = LAST_NAME

        val bookingIds: ArrayList<Int> = Given {
            setQueryParameters(queryParameters.toMap())
        } When {
            get(GET_BOOKING_RESOURCE_PATH)
        } Then {
            validateGetBookings(bookingID)
        } Extract {
            path("findAll { it.bookingid }.bookingid")
        }

        //cross-check the returned booingIDs and their corresponding Booking details
        validateReturnedBookingIDs(bookingIds, FIRST_NAME, LAST_NAME)
    }

    @Test
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-4004 - Verify user get HTTP 200 response when Get Booking details- Filter By Name with invalid values`() {
        val queryParameters = mutableMapOf<String, String>()
        queryParameters[KEY_FIRST_NAME] = "ersd"
        queryParameters[KEY_LAST_NAME] = "erdf"

        Given {
            setQueryParameters(queryParameters.toMap())
        } When {
            get(GET_BOOKING_RESOURCE_PATH)
        } Then {
            validateEmptyBookingsResponse()
        }
    }

    @Test
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-4005 - Verify user get HTTP 400 response when Get Booking details with invalid Parameters - Filter By Name`() {
        val queryParameters = mutableMapOf<String, String>()
        queryParameters["invalid"] = FIRST_NAME
        queryParameters["params"] = LAST_NAME

        Given {
            setQueryParameters(queryParameters.toMap())
        } When {
            get(GET_BOOKING_RESOURCE_PATH)
        } Then {
            validateErrorResponse(400, "Bad Request", "Please check the Query Parameters")
        }
    }

    @Test
    @Tags(Tag(PIPELINE_1), Tag(REGRESSION))
    fun `IDE-4006 - Verify user get HTTP 200 response when Get Booking details - Filter By CheckIn-Out`() {
        val queryParameters = mutableMapOf<String, String>()
        queryParameters[KEY_CHECK_IN] = CHECK_IN
        queryParameters[KEY_CHECK_OUT] = CHECK_OUT

        Given {
            setQueryParameters(queryParameters.toMap())
        } When {
            get(GET_BOOKING_RESOURCE_PATH)
        } Then {
            validateGetBookings(bookingID)
        }

    }
}