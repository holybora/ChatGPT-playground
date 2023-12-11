package com.lvs.chatgpt.ui.stt

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lvs.chatgpt.ui.components.AppBar

@Composable
fun TranscriptionScreen(
    viewModel: TranscriptionViewModel,
    onDrawerClick: () -> Unit
) {

    Log.d("TRANSS", "SHOW")
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        AppBar(
            onClickMenu = { onDrawerClick.invoke() },
            onDeleteClick = {},
            isDeleteAvailable = false
        )
        Divider()

    }
}