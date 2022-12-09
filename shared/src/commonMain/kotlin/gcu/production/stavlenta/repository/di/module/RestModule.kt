package gcu.production.stavlenta.repository.di.module

import gcu.production.stavlenta.repository.di.CommonSDK
import gcu.production.stavlenta.repository.feature.DEFAULT_CLIENT
import gcu.production.stavlenta.repository.feature.REST_MODULE
import gcu.production.stavlenta.repository.rest.data.AuthDataSource
import gcu.production.stavlenta.repository.rest.repository.AuthRepository
import gcu.production.stavlenta.repository.rest.source.AuthSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import kotlin.native.concurrent.ThreadLocal


internal val restModule = DI.Module(name = REST_MODULE, init = {

    bind<AuthSource>() with singleton {
        AuthDataSource(client = instance(DEFAULT_CLIENT))
    }

    bind<AuthRepository>() with singleton {
        AuthRepository(source = instance())
    }
})

@ThreadLocal
object Repositories {
    val restAuthRepository get() = CommonSDK.injections.instance<AuthRepository>()
}

val CommonSDK.repositories get() = Repositories