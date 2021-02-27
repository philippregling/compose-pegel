package com.example.composepegel.network

import retrofit2.Response

sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val error: String?) : Result<T>()
}

fun <T> Response<T>.isSuccess(): Boolean {
    return isSuccessful && body() != null
}

fun <T> Response<T>.getError(): String {
    return errorBody()?.string() ?: ""
}

fun <T> Exception.toResult(): Result<T> {
    return Result.Error(error = this.message)
}