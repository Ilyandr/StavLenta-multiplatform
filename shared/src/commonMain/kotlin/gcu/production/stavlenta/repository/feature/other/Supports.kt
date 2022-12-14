package gcu.production.stavlenta.repository.feature.other

import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import saschpe.kase64.base64Encoded

// Объявление лямбды-выражения для дальнейшей типизации при использовании. (Параметр ф-кции) -> Возвращаемое значение.
// internal - модифиактор доступа только для текущего модуля.
internal typealias faultRequestAction = (HttpStatusCode) -> Unit

internal typealias faultRequestCodeAction = (Int) -> Unit

typealias unitAction = () -> Unit

// Реализация специфичной генерации base64 формата данных для авторизации (login / password).
fun convertToBase64(username: String?, password: String?) =
    "Basic ${("$username:$password".base64Encoded)}"

// Ф-кция-расширение для десереализации любых данных.
fun Any.toJson() = Json.encodeToString(this)
