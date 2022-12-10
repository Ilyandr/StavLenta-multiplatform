package gcu.production.stavlenta.repository.feature.storage


expect class StorageDataSource constructor(): StorageSource {

    override fun getString(key: String): String?
    override fun getBoolean(key: String): Boolean
    override fun getLong(key: String): Long
    override fun getFloat(key: String): Float
    override fun getInt(key: String): Int
    override fun <T> setValue(key: String, value: T)
}