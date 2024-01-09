package com.lvs.data.remote.api.enities


/**
 *
 * Response body for ChatGPT API.
 *
 * see: [https://platform.openai.com/docs/api-reference/chat](https://platform.openai.com/docs/api-reference/chat)
 */
data class SpeechToTextResponseBody(val text: String)