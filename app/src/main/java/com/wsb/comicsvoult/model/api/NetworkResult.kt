package com.wsb.comicsvoult.model.api

import android.os.Message

sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Initial<T>() : NetworkResult<T>()
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String, data: T? = null) : NetworkResult<T>(data, message)
    class Loading<T> : NetworkResult<T>()
}