package com.vitoksmile.shoppinglistkmp.domain.model

import kotlinx.datetime.Instant

data class Item(
    val text: String,
    val createdAt: Instant,
    val completedAt: Instant?,
) {
    val isCompleted: Boolean = completedAt != null
}
