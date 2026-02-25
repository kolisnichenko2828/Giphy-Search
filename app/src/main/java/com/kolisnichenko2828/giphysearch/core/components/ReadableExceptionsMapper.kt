package com.kolisnichenko2828.giphysearch.core.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import coil3.network.HttpException
import com.kolisnichenko2828.giphysearch.R
import com.kolisnichenko2828.giphysearch.core.error.AppException

@Composable
fun Throwable.toUserReadableMessage(): String {
    return when (this) {
        is AppException.NoInternetConnection -> stringResource(R.string.error_no_internet)
        is AppException.RateLimitExceeded -> stringResource(R.string.error_rate_limit)
        is AppException.ServerError -> stringResource(R.string.error_server)
        is HttpException -> stringResource(R.string.error_no_internet)
        is AppException.Timeout -> stringResource(R.string.error_timeout)
        else -> stringResource(R.string.error_unknown)
    }
}