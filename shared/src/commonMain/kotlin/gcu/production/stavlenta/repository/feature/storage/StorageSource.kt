package gcu.production.stavlenta.repository.feature.storage


interface StorageSource {

    fun getString(key: String): String?
    fun getBoolean(key: String): Boolean
    fun getLong(key: String): Long
    fun getFloat(key: String): Float
    fun getInt(key: String): Int
    fun <T> setValue(key: String, value: T)
}