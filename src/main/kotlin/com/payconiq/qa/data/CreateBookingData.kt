package com.payconiq.qa.data

/**
 * Test Data needed for Create booking
 */
class CreateBookingData {

    companion object {
        //Valid Booking details
        const val FIRST_NAME = "Dilshan"
        const val LAST_NAME = "Fernando"
        const val TOTAL_PRICE = 785
        const val DEPOSIT_PAID = true
        const val CHECK_IN = "2018-01-01"
        const val CHECK_OUT = "2019-01-01"
        const val ADDITIONAL_NEEDS = "Breakfast"

        const val UPDATED_FIRST_NAME = "Samantha"
        const val UPDATED_LAST_NAME = "Perera"
        const val UPDATED_TOTAL_PRICE = 321
        const val UPDATED_DEPOSIT_PAID = false

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
         * One of the value is invalid in each data set.
         */
        @JvmStatic
        fun invalidBookingData(): List<Map<String, Any?>> =
            mutableListOf(
                mapOf(
                    Keys.FIRST_NAME to 458,
                    Keys.LAST_NAME to "Fernando",
                    Keys.TOTAL_PRICE to 125,
                    Keys.DEPOSIT_PAID to true,
                    Keys.CHECK_IN to "2018-01-01",
                    Keys.CHECK_OUT to "2019-01-01",
                    Keys.ADDITIONAL_NEEDS to "Breakfast"
                ),
                mapOf(
                    Keys.FIRST_NAME to "Dilshan",
                    Keys.LAST_NAME to 874,
                    Keys.TOTAL_PRICE to 125,
                    Keys.DEPOSIT_PAID to true,
                    Keys.CHECK_IN to "2018-01-01",
                    Keys.CHECK_OUT to "2019-01-01",
                    Keys.ADDITIONAL_NEEDS to "Breakfast"
                ),
                mapOf(
                    Keys.FIRST_NAME to "Dilshan",
                    Keys.LAST_NAME to "Fernando",
                    Keys.TOTAL_PRICE to "125",
                    Keys.DEPOSIT_PAID to true,
                    Keys.CHECK_IN to "2018-01-01",
                    Keys.CHECK_OUT to "2019-01-01",
                    Keys.ADDITIONAL_NEEDS to "Breakfast"
                ),
                mapOf(
                    Keys.FIRST_NAME to "Dilshan",
                    Keys.LAST_NAME to "Fernando",
                    Keys.TOTAL_PRICE to 125,
                    Keys.DEPOSIT_PAID to "true",
                    Keys.CHECK_IN to 784,
                    Keys.CHECK_OUT to "2019-01-01",
                    Keys.ADDITIONAL_NEEDS to "Breakfast"
                ),
                mapOf(
                    Keys.FIRST_NAME to "Dilshan",
                    Keys.LAST_NAME to "Fernando",
                    Keys.TOTAL_PRICE to 125,
                    Keys.DEPOSIT_PAID to true,
                    Keys.CHECK_IN to "2018-01-01",
                    Keys.CHECK_OUT to 9889,
                    Keys.ADDITIONAL_NEEDS to "Breakfast"
                ),
                mapOf(
                    Keys.FIRST_NAME to "Dilshan",
                    Keys.LAST_NAME to "Fernando",
                    Keys.TOTAL_PRICE to 125,
                    Keys.DEPOSIT_PAID to true,
                    Keys.CHECK_IN to "2018-01-01",
                    Keys.CHECK_OUT to "2019-01-01",
                    Keys.ADDITIONAL_NEEDS to 84
                ),
                mapOf(
                    Keys.FIRST_NAME to null,
                    Keys.LAST_NAME to null,
                    Keys.TOTAL_PRICE to null,
                    Keys.DEPOSIT_PAID to null,
                    Keys.CHECK_IN to null,
                    Keys.CHECK_OUT to null,
                    Keys.ADDITIONAL_NEEDS to null
                )
            )

        fun generateValidBookingDetails(): String =
            "{\n" +
                    "  \"firstname\": \"$FIRST_NAME\",\n" +
                    "  \"lastname\": \"$LAST_NAME\", \n" +
                    "  \"totalprice\": $TOTAL_PRICE,\n" +
                    "  \"depositpaid\": $DEPOSIT_PAID,\n" +
                    "  \"bookingdates\": {\n" +
                    "       \"checkin\": \"$CHECK_IN\",\n" +
                    "       \"checkout\": \"$CHECK_OUT\"\n" +
                    "  \n" +
                    "  },\n" +
                    "  \"additionalneeds\": \"$ADDITIONAL_NEEDS\"\n" +
                    "}"
    }
}