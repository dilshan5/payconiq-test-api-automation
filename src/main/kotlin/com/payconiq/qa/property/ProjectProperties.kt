package com.payconiq.qa.property

/**
 * read and store all project properties
 * @author: Dilshan Fernando
 * @since: 06/12/2021
 */
data class ProjectProperties(
    val host: String,
    val environment: String,
    val release: String,
    val client: ClientProperties
)