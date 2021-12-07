package com.payconiq.qa.util

import com.payconiq.qa.property.ProjectProperties
import java.util.*

/**
 * Class containing methods to return configuration parameters based on gradle property input
 * If gradle property regarding environment is not input, returning configuration parameters stated in resource config file
 * @author: Dilshan Fernando
 * @since: 06/12/2021
 */
open class ConfigurationSpecification {

    companion object {

        /**
         * Initializing projectProperties to map the configuration parameters from resource config file to
         * return them if the gradle property input not present
         */
        private val projectProperties: ProjectProperties

        init {
            val inputStream = ConfigurationSpecification::class.java.getResourceAsStream("/config.yaml")
            projectProperties = ObjectMapperUtil.yamlObjectMapper.readValue(inputStream, ProjectProperties::class.java)
        }


        /**
         * Methods to Return configuration parameters
         */
        fun getHost(): String {
            return projectProperties.host
        }

        fun getEnvironment(): String {
            return System.getenv("ENVIRONMENT").lowercase(Locale.getDefault()) ?: projectProperties.environment
        }

        fun getRelease(): String {
            return System.getenv("RELEASE").lowercase(Locale.getDefault()) ?: projectProperties.release
        }

        fun getUserName(): String {
            return System.getenv("USER_NAME") ?: projectProperties.client.userName
        }

        fun getPassword(): String {
            return System.getenv("PASSWORD") ?: projectProperties.client.password
        }
    }
}
