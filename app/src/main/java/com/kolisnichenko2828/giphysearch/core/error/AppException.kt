package com.kolisnichenko2828.giphysearch.core.error

sealed class AppException(cause: Throwable? = null) : Exception(cause) {
    class NoInternetConnection(cause: Throwable? = null) : AppException(cause)
    class RateLimitExceeded(cause: Throwable? = null) : AppException(cause)
    class ServerError(cause: Throwable? = null) : AppException(cause)
    class Unknown(cause: Throwable? = null) : AppException(cause)
}