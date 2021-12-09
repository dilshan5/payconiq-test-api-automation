package com.payconiq.qa.model

import com.payconiq.qa.util.TestBase
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.ValidatableResponse
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers

open class GetBookingIds : TestBase() {

    companion object {
        const val GET_BOOKING_RESOURCE_PATH = "/booking"

        /**
         * Verify the response of Get bookingIDs
         * Each booking should have a bookingID
         * Validate bookingID. Should be integer
         * Should have the last bookingID as well
         */
        fun ValidatableResponse.validateGetBookings(bookingId: Int): ValidatableResponse =
            statusCode(HttpStatus.SC_OK)
                .body("findAll { it.bookingid }.bookingid", CoreMatchers.notNullValue())
                .body("findAll { it.bookingid }.bookingid", CoreMatchers.everyItem(CoreMatchers.isA(Int::class.java)))
                .body("findAll { it.bookingid }.bookingid", CoreMatchers.hasItem(bookingId))

        /**
         * Verify the response of empty Get bookingIDs
         * should be empty array []
         */
        fun ValidatableResponse.validateEmptyBookingsResponse(): ValidatableResponse =
            statusCode(HttpStatus.SC_OK)
                .body("", Matchers.hasSize<Int>(0))

        /**
         * Verify the response of Get specific bookingID
         * Check firstName and lastName
         */
        private fun ValidatableResponse.validateResponseBookingDetailByName(
            firstName: String,
            lastName: String,
        ): ValidatableResponse =
            statusCode(HttpStatus.SC_OK)
                .body("firstname", CoreMatchers.`is`(firstName))
                .body("lastname", CoreMatchers.`is`(lastName))

        /**
         * Verify Get Booking By filters return correct booking ids
         */
        fun validateReturnedBookingIDs(bookingIds: ArrayList<Int>, firstName: String, lastName: String) {
            //iterate all bookingIDs and check the firstName and lastName matched
            for (id in bookingIds) {
                When {
                    get("$GET_BOOKING_RESOURCE_PATH/$id")
                } Then {
                    validateResponseBookingDetailByName(firstName, lastName)
                }
            }
        }

    }
}