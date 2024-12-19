package com.vitoksmile.shoppinglistkmp.domain

import com.vitoksmile.shoppinglistkmp.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun observe(): Flow<List<Item>>

    suspend fun add(text: String): Result<Unit>

    suspend fun complete(item: Item): Result<Unit>
}
