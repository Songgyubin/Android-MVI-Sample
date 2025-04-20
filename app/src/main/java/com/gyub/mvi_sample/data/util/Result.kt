package com.gyub.mvi_sample.data.util

sealed interface Result<out R> {
    data object Loading : Result<Nothing>
    data class Success<out T>(val data: T) : Result<T>
    data object Error : Result<Nothing>
}

inline fun <reified T, reified V> Result<T>.convertIfSuccess(callback: (data: T) -> V): Result<V> {
    return when (this) {
        is Result.Error -> Result.Error
        is Result.Success -> Result.Success(callback(data))
        Result.Loading -> Result.Loading
    }
}

suspend inline fun <T> safeApiCall(crossinline block: suspend () -> T): Result<T> {
    return try {
        Result.Success(block())
    } catch (e: Exception) {
        Result.Error
    }
}