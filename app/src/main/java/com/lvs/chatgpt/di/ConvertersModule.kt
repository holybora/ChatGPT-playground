package com.lvs.chatgpt.di

import com.lvs.data.converters.MessagesDataToUiConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ConvertersModule {

    @Provides
    fun provideMessagesDataToUiConverter() : MessagesDataToUiConverter = MessagesDataToUiConverter()
}