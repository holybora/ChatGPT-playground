package com.lvs.data.remote.common

enum class GPTModel(val value: String, val maxTokens: Int) {
    GPT35TURBO("gpt-3.5-turbo", 4096),
    GPT4("gpt-4", 4096),
    GPT4_1106_PREVIEW("gpt-4-1106-preview", 4096)
}