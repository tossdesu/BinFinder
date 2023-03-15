package com.tossdesu.bankcardinfo.data.network

import com.tossdesu.bankcardinfo.domain.Resource
import com.tossdesu.bankcardinfo.domain.Resource.Exception.Cause
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Wrapping class for handling exceptions coming from
 * Retrofit when execute api request.
 * @return Success|Exception object of [Resource] sealed class
 */
class SafeApiCall @Inject constructor() {

    suspend fun <T> run(block: suspend () -> T): Resource<T> {
        return try {
            // calling api lambda
            Resource.Success(block())
        } catch (e: Exception) {
            when(e) {
                // retrofit
                is HttpException -> {
                    val code = e.code()
                    if (code == 400 || code == 404) {
                        Resource.Exception(Cause.HttpResponseNothingFound)
                    } else {
                        Resource.Exception(Cause.HttpException(code, e.message()))
                    }
                }
                // network connection
                is IOException -> {
                    Resource.Exception(Cause.NoConnection)
                }
                // unknown error
                else -> {
                    Resource.Exception(Cause.Unknown(e.message ?: ""))
                }
            }
        }
    }
}