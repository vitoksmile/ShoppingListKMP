package com.vitoksmile.shoppinglistkmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform