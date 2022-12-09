package gcu.production.stavlenta.repository.di

import gcu.production.stavlenta.repository.di.module.coreModule
import gcu.production.stavlenta.repository.di.module.restModule
import org.kodein.di.*
import kotlin.native.concurrent.ThreadLocal


@ThreadLocal
object CommonSDK {

    val injections by lazy { directDI }

    private var directDI: DirectDI = DI { importAll(restModule, coreModule) }.direct
}