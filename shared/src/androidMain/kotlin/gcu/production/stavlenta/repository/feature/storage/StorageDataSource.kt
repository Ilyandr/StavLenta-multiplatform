package gcu.production.stavlenta.repository.feature.storage

import android.content.Context
import android.content.SharedPreferences


actual class StorageDataSource actual constructor() : StorageSource {

    private var preferences: SharedPreferences? = null
    private val editor by lazy { preferences?.edit() }


    constructor(context: Context) : this() {
        preferences = context.getSharedPreferences(null, Context.MODE_PRIVATE)
    }

    actual override fun getString(key: String) = preferences?.getString(key, null)
    actual override fun getBoolean(key: String) = preferences?.getBoolean(key, false) ?: false
    actual override fun getLong(key: String) = preferences?.getLong(key, -1) ?: -1
    actual override fun getFloat(key: String) = preferences?.getFloat(key, -1f) ?: -1f
    actual override fun getInt(key: String) = preferences?.getInt(key, -1) ?: -1

    actual override fun <T> setValue(key: String, value: T) {
        when (value) {
            is Int -> editor?.putInt(key, value)?.apply()
            is Float -> editor?.putFloat(key, value)?.apply()
            is Long -> editor?.putLong(key, value)?.apply()
            is Boolean -> editor?.putBoolean(key, value)?.apply()
            is String? -> editor?.putString(key, value)?.apply()
            else -> throw IllegalArgumentException("Unknown value type")
        }
    }
}