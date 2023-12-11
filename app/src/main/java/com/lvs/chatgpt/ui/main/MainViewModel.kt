package com.lvs.chatgpt.ui.main

import android.util.Log
import com.lvs.chatgpt.base.BaseViewModel
import com.lvs.chatgpt.base.UiEvent
import com.lvs.chatgpt.base.UiState
import com.lvs.data.remote.db.entities.ConversationEntity
import com.lvs.domain.DeleteConversationUseCase
import com.lvs.domain.GetConversationsFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getConversationsFlow: GetConversationsFlowUseCase,
    private val deleteConversationUseCase: DeleteConversationUseCase
) : BaseViewModel<MainEvent, MainUiState>() {

    private var isFirstRun = true

    init {
        Log.d(tag, "ViewModel created")
        launchOnBackground {
            getConversationsFlow()
                .collect {
                    setState {
                        if (isFirstRun) {
                            isFirstRun = false
                            copy(
                                conversations = it,
                                openConversation = if (it.isNotEmpty()) OpenConversationSideEffect(it.first().id) else null
                            )
                        } else
                            copy(conversations = it)
                    }
                }
        }
    }

    override val tag: String
        get() = HomeViewModel::class.java.name

    override fun createInitialState(): MainUiState = MainUiState()

    override fun handleEvent(event: MainEvent) {
        when (event) {
            is MainEvent.OnChatClicked -> {
                setState { copy(openConversation = OpenConversationSideEffect(conversationId = checkNotNull(event.conversation?.id))) }
            }

            is MainEvent.OnNewChatClicked -> {
                setState { copy(openConversation = null) }
            }

            is MainEvent.OnDeleteChatClicked -> {
                launchOnBackground {
                    val conversation = event.conversation
                    if (conversation == null) return@launchOnBackground
                    else {
                        //delete from db
                        runCatching { deleteConversationUseCase(conversation.id) }
                            .onFailure {
                                when (it) {
                                    is DeleteConversationUseCase.ConversationNonExistException -> {
                                        //TODO: show personal message to user about error
                                    }
                                }
                            }
                            .getOrThrow()

                        val conversations = uiState.value.conversations
                        val index = uiState.value.conversations.indexOfFirst { it.id == event.conversation.id }
                        when {
                            //deleted single conv, set to default
                            conversations.isEmpty() || conversations.size == 1 ->
                                setState {
                                    copy(conversations = emptyList(), openConversation = null)
                                }

                            //deleted last, fetch previous
                            conversations.lastIndex == index ->
                                setState {
                                    copy(
                                        openConversation = OpenConversationSideEffect(
                                            conversations[conversations.lastIndex - 1].id
                                        )
                                    )
                                }

                            //fetch next in turn
                            else -> setState {
                                copy(
                                    openConversation = OpenConversationSideEffect(
                                        conversations[index + 1].id
                                    )
                                )
                            }
                        }
                    }

                }
            }

            is MainEvent.OnNewChatCreated -> setState {
                copy(
                    openConversation = OpenConversationSideEffect(
                        conversationId = event.convId,
                        isNew = true
                    )
                )
            }

        }

    }
}


data class MainUiState(
    val conversations: List<ConversationEntity> = emptyList(),
    val openConversation: OpenConversationSideEffect? = null
) : UiState

data class OpenConversationSideEffect(val conversationId: Long, val isNew: Boolean = false)

sealed interface MainEvent : UiEvent {
    class OnChatClicked(val conversation: ConversationEntity?) : MainEvent
    object OnNewChatClicked : MainEvent
    class OnNewChatCreated(val convId: Long) : MainEvent
    class OnDeleteChatClicked(val conversation: ConversationEntity?) : MainEvent

}