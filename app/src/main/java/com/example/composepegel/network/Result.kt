package com.example.composepegel.network

import retrofit2.Response

sealed class Result<T> {
    data class Success<T>(val data : T) : Result<T>()
    data class Error<T>(val error: String?) : Result<T>()
}

fun <T> Response<T>.toResult() : Result<T> {
    return if(this.isSuccessful) {
        Result.Success(data = this.body()!!)
    } else {
        Result.Error(error = this.errorBody()?.string())
    }
}

fun <T> Exception.toResult() : Result<T> {
    return Result.Error(error = this.message)
}