package gcu.production.stavlenta.repository.di.module

import gcu.production.stavlenta.repository.feature.CORE_MODULE
import org.kodein.di.DI


val coreModule = DI.Module(name = CORE_MODULE, init = {
    importAll(
        ktorModule, serializationModule
    )
})