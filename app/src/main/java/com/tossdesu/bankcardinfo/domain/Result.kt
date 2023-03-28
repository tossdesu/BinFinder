package com.tossdesu.bankcardinfo.domain

/**
 * Sealed class for observe state of Retrofit|Room response
 * [Success]    - successful response, returning data
 * [Error]      - domain layer errors that are related to the business logic
 * [Exception]  - type of exception that related with network (Retrofit)
 * [Exception.Cause.NoConnection] - IOException
 * [Exception.Cause.HttpResponseNothingFound] - 404, 400 HttpException handling
 * [Exception.Cause.HttpException] - Others HttpException, passing code and message
 * [Exception.Cause.DatabaseException] - All database exceptions, passing message
 * [Exception.Cause.Unknown] - Unknown Exception, passing message
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val messageStringResource: Int) : Result<Nothing>()
    data class Exception(val cause: Cause) : Result<Nothing>() {
        sealed class Cause {
            object NoConnection : Cause()
            object HttpResponseNothingFound : Cause()
            data class HttpException(val code: Int, val message: String) : Cause()
            data class DatabaseException(val message: String) : Cause()
            data class Unknown(val message: String) : Cause()
        }
    }
}
