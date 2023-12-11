package com.lvs.chatgpt.ui.stt

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lvs.chatgpt.ui.components.AppBar
import com.lvs.chatgpt.ui.components.NiaButton

@Composable
fun TranscriptionScreen(
    viewModel: TranscriptionViewModel = hiltViewModel(),
    onDrawerClick: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val result = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        result.value = it
    }

    if (uiState.selectingVideo) {
        launcher.launch(PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.VideoOnly))
        viewModel.setEvent(TranscriptionEvent.OnSelectVideoShowed)
    }

    result.value?.let {
        viewModel.setEvent(TranscriptionEvent.OnVideoSelected(it))
    }

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


        Box(modifier = Modifier.fillMaxSize()) {
            NiaButton(
                onClick = { viewModel.setEvent(TranscriptionEvent.OnUploadClicked) },
                text = { Text("Upload file") },
                leadingIcon = { Icon(imageVector = Icons.Outlined.Add, contentDescription = null) },
                modifier = Modifier.align(Alignment.Center)
            )
        }


    }
}

@Preview
@Composable
fun Preivew() {
    TranscriptionScreen {

    }
}