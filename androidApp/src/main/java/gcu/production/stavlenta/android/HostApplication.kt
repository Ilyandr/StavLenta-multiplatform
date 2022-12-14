package gcu.production.stavlenta.android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import gcu.production.stavlenta.repository.di.CommonSDK
import gcu.production.stavlenta.repository.feature.storage.StorageDataSource


@HiltAndroidApp
class HostApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CommonSDK.storageSource = StorageDataSource(applicationContext)
    }
}