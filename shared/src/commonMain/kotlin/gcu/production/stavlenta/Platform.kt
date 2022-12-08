package gcu.production.stavlenta

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform