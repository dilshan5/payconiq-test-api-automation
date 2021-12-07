package com.payconiq.qa.data

import java.util.stream.Stream

/**
 * Test Data needed for Create booking
 */
class CreateBookingData {

    companion object {
        object Keys {
            const val FIRST_NAME = "firstname"
            const val LAST_NAME = "lastname"
            const val TOTAL_PRICE = "totalprice"
            const val DEPOSIT_PAID = "depositpaid"
            const val CHECK_IN = "checkin"
            const val CHECK_OUT = "checkout"
            const val ADDITIONAL_NEEDS = "additionalneeds"
        }

        /**
         * Method returns a map to be used as test data in parameterized tests. This method returns a data set
         * with api firstname, lastname, totalprice, depositpaid, checkin, checkout and additionalneeds.
         */
        @JvmStatic
        fun validBookingData(): Stream<Map<String, Any>> =
            Stream.of(
                mapOf(
                    Keys.FIRST_NAME to "Dilshan",
                    Keys.LAST_NAME to "Fernando",
                    Keys.TOTAL_PRICE to 125,
                    Keys.DEPOSIT_PAID to true,
                    Keys.CHECK_IN to "2018-01-01",
                    Keys.CHECK_OUT to "2019-01-01",
                    Keys.ADDITIONAL_NEEDS to "Breakfast"
                )
            )
    }
}