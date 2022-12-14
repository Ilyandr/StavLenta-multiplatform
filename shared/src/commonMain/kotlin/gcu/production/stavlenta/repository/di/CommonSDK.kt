package gcu.production.stavlenta.repository.di

import gcu.production.stavlenta.repository.di.module.clientModule
import gcu.production.stavlenta.repository.di.module.restModule
import gcu.production.stavlenta.repository.di.module.serviceModule
import gcu.production.stavlenta.repository.feature.storage.StorageSource
import gcu.production.stavlenta.repository.rest.repository.AuthRepository
import gcu.production.stavlenta.repository.rest.repository.ContentRepository
import org.kodein.di.*
import kotlin.native.concurrent.ThreadLocal

// Static-класс для обращения из нативных модулей. Является проводником доступа к кроссплатформенной бизнес-логике.
// Является потокобезопасным (аннотация @ThreadLocal).
@ThreadLocal
object CommonSDK {

    // Обращение к иницализации di. (lazy - инициализация при первом вызове).
    private val injections by lazy { directDI }

    // Иницализация di.
    private var directDI: DirectDI = DI {
        importAll(restModule, serviceModule, clientModule)
    }.direct


    // Основые репозитории / source для кроссплатформенного взаимодействия.
    // get - обращение к инициализации переменной при каждом вызове. (утечки памяти невозможны - di by singleton).
    val restAuthRepository get() = injections.instance<AuthRepository>()
    val restContentRepository get() = injections.instance<ContentRepository>()
    var storageSource = injections.instance<StorageSource>()
}