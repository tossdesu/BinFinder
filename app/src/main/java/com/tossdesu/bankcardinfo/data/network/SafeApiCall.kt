package com.tossdesu.bankcardinfo.data.network

import com.tossdesu.bankcardinfo.domain.Result
import com.tossdesu.bankcardinfo.domain.Result.Exception.Cause
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Wrapping class for handling exceptions coming from
 * Retrofit when execute api request.
 * @return Success|Exception object of [Result] sealed class
 */
class SafeApiCall @Inject constructor() {

    suspend fun <T> run(block: suspend () -> T): Result<T> {
        return try {
            // calling api lambda
            Result.Success(block())
        } catch (e: Exception) {
            when(e) {
                // retrofit
                is HttpException -> {
                    val code = e.code()
                    if (code == 404) {
                        Result.Exception(Cause.HttpResponseNothingFound)
                    } else {
                        Result.Exception(Cause.HttpException(code, e.message()))
                    }
                }
                // network connection
                is IOException -> {
                    Result.Exception(Cause.NoConnection)
                }
                // unknown error
                else -> {
                    Result.Exception(Cause.Unknown(e.message ?: ""))
                }
            }
        }
    }
}