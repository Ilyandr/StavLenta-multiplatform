package gcu.production.stavlenta.repository.feature

import io.ktor.http.*
import saschpe.kase64.base64Encoded

internal typealias faultRequestAction = (HttpStatusCode) -> Unit

internal fun Pair<String?, String?>.requireAuthToken() = "Basic ${("$first:$second".base64Encoded)}"