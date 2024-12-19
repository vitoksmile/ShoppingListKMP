package com.vitoksmile.shoppinglistkmp.data

import com.vitoksmile.shoppinglistkmp.domain.Repository
import com.vitoksmile.shoppinglistkmp.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.random.Random
import kotlin.time.Duration.Companion.hours

class RepositoryImpl : Repository {

    private val inMemoryCache = MutableStateFlow<List<Item>>(emptyList())

    init {
        inMemoryCache.update { fakeItems() }
    }

    override fun observe(): Flow<List<Item>> =
        inMemoryCache
            .map { it.sortedWith(comparator) }

    override suspend fun add(text: String): Result<Unit> = runCatching {
        inMemoryCache.update { cache ->
            cache.toMutableList()
                .apply {
                    add(
                        Item(
                            text = text,
                            createdAt = now(),
                            completedAt = null,
                        )
                    )
                }
        }
    }

    override suspend fun complete(item: Item): Result<Unit> = runCatching {
        inMemoryCache.update { cache ->
            // Ensure we have the required item in the cache
            val itemToComplete = cache.first { it.createdAt == item.createdAt }

            // Verify the item is not completed yet
            require(itemToComplete.completedAt == null) { "Item must not be completed yet" }

            val indexToUpdate = cache.indexOf(itemToComplete)
            require(indexToUpdate >= 0) { "Failed to find item in cache" }
            cache.toMutableList()
                .apply {
                    set(indexToUpdate, item.copy(completedAt = now()))
                }
        }
    }

    private fun now(): Instant = Clock.System.now()

    private fun fakeItems(): List<Item> = List(20) {
        Item(
            text = "Item $it",
            createdAt = Clock.System.now().minus(Random.nextInt(1, 100).hours),
            // complete each 3rd item
            completedAt = if (it % 3 == 0) {
                Clock.System.now().minus(Random.nextInt(1, 100).hours)
            } else {
                null
            },
        )
    }

    /**
     * Display completed items at the end and other items sorted by date when
     * there were created to show newest item on the top.
     */
    private val comparator: Comparator<Item> =
        // Completed items at the end
        compareBy<Item> { it.completedAt }
            // Newest item on top
            .thenByDescending { it.createdAt }

}
