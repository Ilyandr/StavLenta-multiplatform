package gcu.production.stavlenta.repository.di.module

import gcu.production.stavlenta.repository.feature.other.AUTH_CLIENT
import gcu.production.stavlenta.repository.feature.other.DEFAULT_CLIENT
import gcu.production.stavlenta.repository.feature.other.REST_MODULE
import gcu.production.stavlenta.repository.rest.data.AuthDataSource
import gcu.production.stavlenta.repository.rest.data.ContentDataSource
import gcu.production.stavlenta.repository.rest.repository.AuthRepository
import gcu.production.stavlenta.repository.rest.repository.ContentRepository
import gcu.production.stavlenta.repository.rest.source.AuthSource
import gcu.production.stavlenta.repository.rest.source.ContentSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

// Реализация модуля di. (restModule - инициализация source by dataSource).
internal val restModule = DI.Module(name = REST_MODULE, init = {

    bind<AuthSource>() with singleton {
        AuthDataSource(client = instance(DEFAULT_CLIENT))
    }

    bind<ContentSource>() with singleton {
        ContentDataSource(client = instance(AUTH_CLIENT))
    }

    bind<AuthRepository>() with singleton {
        AuthRepository(source = instance())
    }

    bind<ContentRepository>() with singleton {
        ContentRepository(source = instance())
    }
})