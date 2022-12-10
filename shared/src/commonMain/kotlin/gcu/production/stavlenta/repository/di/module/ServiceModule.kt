package gcu.production.stavlenta.repository.di.module

import gcu.production.stavlenta.repository.feature.other.SERVICE_MODULE
import gcu.production.stavlenta.repository.feature.storage.StorageDataSource
import gcu.production.stavlenta.repository.feature.storage.StorageSource
import gcu.production.stavlenta.repository.rest.core.HttpEngineFactory
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton


val serviceModule = DI.Module(name = SERVICE_MODULE, init = {

    bind<StorageSource>() with singleton { StorageDataSource() }
})