package com.payconiq.qa.model

import com.payconiq.qa.util.TestBase
import io.restassured.response.ValidatableResponse
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers

class PartialUpdateBooking : TestBase() {

    companion object {
        const val UPDATE_BOOKING_RESOURCE_PATH = "/booking"
        var accessToken = ""

        /**
         * Method for checking response bodies & headers for update Booking
         */
        fun ValidatableResponse.validateResponseUpdateBooking(
            firstName: String,
            lastName: String,
            totalPrice: Int,
            depositPaid: Boolean,
            checkIN: String,
            checkOUT: String,
            additionalNeeds: String
        ): ValidatableResponse =
            statusCode(HttpStatus.SC_OK)
                .body("booking.firstname", CoreMatchers.`is`(firstName))
                .body("booking.lastname", CoreMatchers.`is`(lastName))
                .body("booking.totalprice", CoreMatchers.`is`(totalPrice))
                .body("booking.depositpaid", CoreMatchers.`is`(depositPaid))
                .body("booking.bookingdates.checkin", CoreMatchers.`is`(checkIN))
                .body("booking.bookingdates.checkout", CoreMatchers.`is`(checkOUT))
                .body("booking.additionalneeds", CoreMatchers.`is`(additionalNeeds))

    }
}