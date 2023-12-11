package com.lvs.data.remote.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    val createdAt: String,
) {
    data class InsertionPrototype(val title: String)

    data class UpdateTitle(
        val id: Long,
        val title: String,
    )

    companion object {
        val NEW_EMPTY_CONVERSATION_ID = -1L
    }
}

fun ConversationEntity.toUpdateTitle(newTitle: String) = ConversationEntity.UpdateTitle(this.id, title = newTitle)
