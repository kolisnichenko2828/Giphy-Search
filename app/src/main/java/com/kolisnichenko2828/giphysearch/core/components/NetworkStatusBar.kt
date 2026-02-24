package com.kolisnichenko2828.giphysearch.core.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kolisnichenko2828.giphysearch.R
import kotlinx.coroutines.delay

@Composable
fun NetworkStatusBar(isNetworkAvailable: Boolean) {
    var wasDisconnected by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(isNetworkAvailable) {
        if (!isNetworkAvailable) {
            wasDisconnected = true
        } else if (wasDisconnected) {
            delay(3000)
            wasDisconnected = false
        }
    }

    AnimatedVisibility(
        visible = !isNetworkAvailable || (wasDisconnected),
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        val backgroundColor = if (!isNetworkAvailable) {
            Color.Red
            Color(0xFFa60013)
        } else {
            Color(0xFF4CAF50)
        }

        val message = if (!isNetworkAvailable) {
            stringResource(R.string.no_connection)
        } else {
            stringResource(R.string.back_online)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                color = Color.White,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}