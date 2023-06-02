package com.lvs.chatgpt.ui.main

import com.lvs.chatgpt.base.UiEvent

sealed class MainEvent : UiEvent {
    class OnChatClicked(val chatId: Long) : MainEvent()
    object OnNewChatClicked : MainEvent()
    object OnOpenDrawer : MainEvent()
    object OnResetOpenDrawerAction : MainEvent()

    class OnSendMessage(val message: String) : MainEvent()
}