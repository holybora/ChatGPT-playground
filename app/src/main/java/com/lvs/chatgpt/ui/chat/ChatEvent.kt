package com.lvs.chatgpt.ui.chat

import com.lvs.chatgpt.base.UiEvent

sealed class ChatEvent : UiEvent {
    class OnChatClicked(val chatId: Long) : ChatEvent()
    object OnNewChatClicked : ChatEvent()
    class OnSendMessage(val message: String) : ChatEvent()
}