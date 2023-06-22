package com.lvs.chatgpt.ui.chat

import com.lvs.chatgpt.base.UiEvent

sealed class ChatEvent : UiEvent {
    class OnSendMessage(val message: String) : ChatEvent()
}