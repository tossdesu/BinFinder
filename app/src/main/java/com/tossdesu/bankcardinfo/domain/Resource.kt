package com.tossdesu.bankcardinfo.domain

/**
 * Sealed class for observe state of api response
 * [Success]    - successful response, returning data
 * [Exception]  - type of exception that related with network (Retrofit)
 * [Exception.Cause.NoConnection] - IOException
 * [Exception.Cause.HttpResponseNothingFound] - 404, 400 HttpException handling
 * [Exception.Cause.HttpException] - Others HttpException, passing code and message
 * [Exception.Cause.Unknown] - Unknown Exception, passing message
 */
sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Exception(val cause: Cause) : Resource<Nothing>() {
        sealed class Cause {
            object NoConnection : Cause()
            object HttpResponseNothingFound : Cause()
            data class HttpException(val code: Int, val message: String) : Cause()
            data class Unknown(val message: String) : Cause()
        }
    }
}
