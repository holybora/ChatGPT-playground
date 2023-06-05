package com.lvs.data.local.main

import com.lvs.data.remote.common.GPTRole
import java.util.Date


data class MessageUiEntity(
    val uid: Long,
    val conversationId: Long,
    val text: String,
    val role: GPTRole,
    val createdAt: String
)