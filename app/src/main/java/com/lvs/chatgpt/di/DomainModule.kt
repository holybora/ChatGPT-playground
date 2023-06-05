package com.lvs.chatgpt.di

import com.lvs.data.converters.MessagesDataToUiConverter
import com.lvs.data.remote.repositories.ConversationRepository
import com.lvs.data.remote.repositories.MessageRepository
import com.lvs.data.remote.repositories.OpenAIRepository
import com.lvs.domain.CreateConversationUseCase
import com.lvs.domain.GetConversationUseCase
import com.lvs.domain.GetConversationsFlowUseCase
import com.lvs.domain.GetMessagesByConversationIdUseCase
import com.lvs.domain.SendMessageToChatGPTUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @Provides
    fun provideCreateConversationUseCase(conversationRepository: ConversationRepository) =
        CreateConversationUseCase(conversationRepository)

    @Provides
    fun provideGetConversationUseCase(conversationRepository: ConversationRepository) =
        GetConversationUseCase(conversationRepository)

    @Provides
    fun provideSendMessageToGPTUseCase(
        messagesRepository: MessageRepository,
        openAIRepository: OpenAIRepository,
        conversationRepository: ConversationRepository,
        messagesDataToUiConverter: MessagesDataToUiConverter
    ) =
        SendMessageToChatGPTUseCase(
            conversationRepository,
            messagesRepository,
            openAIRepository,
            messagesDataToUiConverter
        )

    @Provides
    fun provideGetMessagesByConversationIdUseCase(
        messagesRepository: MessageRepository,
        messagesDataToUiConverter: MessagesDataToUiConverter
    ) =
        GetMessagesByConversationIdUseCase(messagesRepository, messagesDataToUiConverter)

    @Provides
    fun provideConversationsFlowUseCase(conversationRepository: ConversationRepository) =
        GetConversationsFlowUseCase(conversationRepository)

}