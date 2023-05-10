package com.lvs.domain.models

import java.util.*

data class MessageModel(
    var id: String = Date().time.toString(),
    var conversationId: String = "",
    var question: String = "",
    var answer: String = "",
    var createdAt: Date = Date(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MessageModel

        return when {
            id != other.id -> false
            conversationId != other.conversationId -> false
            question != other.question -> false
            answer != other.answer -> false
            createdAt !== other.createdAt -> false
            else -> true
        }
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + conversationId.hashCode()
        result = 31 * result + question.hashCode()
        result = 31 * result + answer.hashCode()
        result = 31 * result + createdAt.hashCode()
        return result
    }
}