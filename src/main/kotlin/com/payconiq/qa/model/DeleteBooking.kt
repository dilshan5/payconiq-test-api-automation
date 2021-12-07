package com.payconiq.qa.model

import io.restassured.response.ValidatableResponse
import org.apache.http.HttpStatus

class DeleteBooking {

    companion object {
        const val DELETE_BOOKING_RESOURCE_PATH = "/booking"

        /**
         * Verify the response of DELETE booking
         * https://datatracker.ietf.org/doc/html/rfc7231#section-4.3.5
         * As per the RFC, delete response should return HTTP 204, assuming no further information is to be supplied
         */
        fun ValidatableResponse.validateResponseDeleteBooking(): ValidatableResponse =
            statusCode(HttpStatus.SC_NO_CONTENT)
    }
}