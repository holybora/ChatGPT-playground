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
                                navigationSideEffect =
                                if (it.isNotEmpty()) NavigationSideEffect.OpenConversationEffect(it.first().id)
                                else NavigationSideEffect.OpenBlankChatEffect
                            )
                        } else
                            copy(conversations = it)
                    }
                }
        }
    }

    override val tag: String
        get() = HomeViewModel::class.java.name

    override fun createInitialState(): MainUiState =
        MainUiState(navigationSideEffect = null)

    override fun handleEvent(event: MainEvent) {
        when (event) {
            is MainEvent.OnChatClicked -> {
                Log.d(tag, "onChatClicked event: ${event.conversation?.id}")
                setState {
                    copy(
                        navigationSideEffect = NavigationSideEffect.OpenConversationEffect(
                            conversationId = checkNotNull(
                                event.conversation?.id
                            )
                        )
                    )
                }
            }

            is MainEvent.OnNewChatClicked -> {
                setState { copy(navigationSideEffect = NavigationSideEffect.OpenBlankChatEffect) }
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
                                    copy(
                                        conversations = emptyList(),
                                        navigationSideEffect = NavigationSideEffect.OpenBlankChatEffect
                                    )
                                }

                            //deleted last, fetch previous
                            conversations.lastIndex == index ->
                                setState {
                                    copy(
                                        navigationSideEffect = NavigationSideEffect.OpenConversationEffect(
                                            conversations[conversations.lastIndex - 1].id
                                        )
                                    )
                                }

                            //fetch next in turn
                            else -> setState {
                                copy(
                                    navigationSideEffect = NavigationSideEffect.OpenConversationEffect(
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
                    navigationSideEffect = NavigationSideEffect.OpenConversationEffect(
                        conversationId = event.convId,
                        isNew = true
                    )
                )
            }

            MainEvent.DisposeSideEffect -> {
                setState { copy(navigationSideEffect = null) }
            }

            MainEvent.OnTranscriptionClicked -> {
                setState { copy(navigationSideEffect = NavigationSideEffect.OpenTranscriptionEffect) }
            }
        }

    }
}


data class MainUiState(
    val conversations: List<ConversationEntity> = emptyList(),
    val navigationSideEffect: NavigationSideEffect?
) : UiState


sealed interface NavigationSideEffect {
    class OpenConversationEffect(val conversationId: Long, val isNew: Boolean = false) : NavigationSideEffect
    object OpenBlankChatEffect : NavigationSideEffect
    object OpenTranscriptionEffect : NavigationSideEffect
}


sealed interface MainEvent : UiEvent {
    class OnChatClicked(val conversation: ConversationEntity?) : MainEvent
    object OnNewChatClicked : MainEvent
    class OnNewChatCreated(val convId: Long) : MainEvent
    class OnDeleteChatClicked(val conversation: ConversationEntity?) : MainEvent

    object DisposeSideEffect : MainEvent
    object OnTranscriptionClicked : MainEvent

}