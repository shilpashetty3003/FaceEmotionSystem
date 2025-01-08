package com.example.fermusicsystem.common

sealed class ResponseResult<out T>{
    object Loading :ResponseResult<Nothing>()
    data class Success<T> (val data:T):ResponseResult<T>()
    data class Failure(val error:String) :ResponseResult<Nothing>()
}
