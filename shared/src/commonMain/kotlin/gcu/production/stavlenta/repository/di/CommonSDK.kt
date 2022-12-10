package gcu.production.stavlenta.repository.di

import gcu.production.stavlenta.repository.di.module.clientModule
import gcu.production.stavlenta.repository.di.module.restModule
import gcu.production.stavlenta.repository.di.module.serviceModule
import gcu.production.stavlenta.repository.feature.storage.StorageSource
import gcu.production.stavlenta.repository.rest.repository.AuthRepository
import gcu.production.stavlenta.repository.rest.repository.ContentRepository
import org.kodein.di.*
import kotlin.native.concurrent.ThreadLocal


@ThreadLocal
object CommonSDK {

    private val injections by lazy { directDI }

    private var directDI: DirectDI = DI {
        importAll(restModule, serviceModule, clientModule)
    }.direct


    val restAuthRepository get() = injections.instance<AuthRepository>()
    val restContentRepository get() = injections.instance<ContentRepository>()
    val storageSource get() = injections.instance<StorageSource>()
}