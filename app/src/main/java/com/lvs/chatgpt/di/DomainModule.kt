package com.lvs.chatgpt.di

import android.content.Context
import com.lvs.data.converters.MessagesDataToUiConverter
import com.lvs.data.repositories.ConversationRepository
import com.lvs.data.repositories.MessageRepository
import com.lvs.data.repositories.OpenAIRepository
import com.lvs.domain.CreateConversationUseCase
import com.lvs.domain.DeleteConversationUseCase
import com.lvs.domain.GetAudioFromVideoUseCase
import com.lvs.domain.GetConversationsFlowUseCase
import com.lvs.domain.GetFilePathFromUriUseCase
import com.lvs.domain.GetMessagesByConversationIdUseCase
import com.lvs.domain.InsertMessageUseCase
import com.lvs.domain.SendMessageToChatGPTUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @Provides
    fun provideCreateConversationUseCase(conversationRepository: ConversationRepository) =
        CreateConversationUseCase(conversationRepository)

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

    @Provides
    fun provideInsertMessageUseCase(
        messagesRepository: MessageRepository,
        messagesDataToUiConverter: MessagesDataToUiConverter
    ) = InsertMessageUseCase(
        messagesRepository = messagesRepository,
        messagesDataToUiConverter = messagesDataToUiConverter
    )

    @Provides
    fun provideDeleteConversationUseCase(
        conversationRepository: ConversationRepository
    ) = DeleteConversationUseCase(conversationRepository)

    @Provides
    fun provideGetFilePathFromUriUseCase(@ApplicationContext context: Context) =
        GetFilePathFromUriUseCase(context)

    @Provides
    fun provideGetAudioFromVideoUseCase() = GetAudioFromVideoUseCase()

}