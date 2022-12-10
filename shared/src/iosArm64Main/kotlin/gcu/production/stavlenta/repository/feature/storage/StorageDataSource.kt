package gcu.production.stavlenta.repository.feature.storage

import platform.Foundation.NSUserDefaults
import platform.Foundation.setValue
import kotlin.reflect.KClass

actual class StorageDataSource actual constructor(): StorageSource {

    private val basicDataController by lazy { NSUserDefaults.standardUserDefaults() }


    actual override fun getString(key: String): String? {
        TODO("Not yet implemented")
    }

    actual override fun getBoolean(key: String): Boolean {
        TODO("Not yet implemented")
    }

    actual override fun getLong(key: String): Long {
        TODO("Not yet implemented")
    }

    actual override fun getFloat(key: String): Float {
        TODO("Not yet implemented")
    }

    actual override fun getInt(key: String): Int {
        TODO("Not yet implemented")
    }


    actual override fun <T> setValue(key: String, value: T) {
    }
}