package com.lvs.data.remote.api.enities

import com.google.gson.annotations.SerializedName


/**
 *
 * Response body for ChatGPT API.
 *
 * see: [https://platform.openai.com/docs/api-reference/chat](https://platform.openai.com/docs/api-reference/chat)
 */
data class ChatCompletionResponseBody(
    @SerializedName(value = "id")
    var id: String,

    @SerializedName(value = "object")
    var `object`: String,

    @SerializedName(value = "created")
    var created: Long,

    @SerializedName(value = "model")
    var model: String,

    @SerializedName(value = "choices")
    var choices: List<Choice>,

    @SerializedName(value = "usage")
    var usage: Usage,
) {

    data class Choice(
        @SerializedName(value = "index")
        var index: Int,

        @SerializedName(value = "message")
        var message: MessageDto,

        @SerializedName(value = "finish_reason")
        var finishReason: String
    )
    data class Usage(
        @SerializedName(value = "prompt_tokens")
        var promptTokens: Int,

        @SerializedName(value = "completion_tokens")
        var completionTokens: Int,

        @SerializedName(value = "total_tokens")
        var totalTokens: Int,
    )
}

data class MessageDto(val role: String, val content: String)
