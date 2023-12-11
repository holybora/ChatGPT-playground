package com.lvs.chatgpt.ui.stt

import android.net.Uri
import com.lvs.chatgpt.base.UiEvent


sealed interface TranscriptionEvent : UiEvent {
    object OnUploadClicked : TranscriptionEvent
    object OnSelectVideoShowed : TranscriptionEvent
    data class OnVideoSelected(val uri: Uri) : TranscriptionEvent
}