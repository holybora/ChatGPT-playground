package com.lvs.domain

import com.lvs.data.repositories.OpenAIRepository
import javax.inject.Inject

class GetTextFromAudioFileUseCase @Inject constructor(val openAIRepository: OpenAIRepository) {

    suspend operator fun invoke(filePath: String): String {
        return openAIRepository.speechToText(filePath).text
    }
}