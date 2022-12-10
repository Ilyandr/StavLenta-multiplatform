package gcu.production.stavlenta.repository.feature.other

import gcu.production.stavlenta.repository.feature.source.BaseEntity
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import saschpe.kase64.base64Encoded


internal typealias faultRequestAction = (HttpStatusCode) -> Unit

internal typealias faultRequestCodeAction = (Int) -> Unit

internal typealias voidAction = () -> Unit


internal fun convertToBase64(username: String?, password: String?) = "Basic ${("$username:$password".base64Encoded)}"

fun Any.toJson() = Json.encodeToString(this)

inline fun HttpResponse.completableRequest(
    crossinline onSuccess: voidAction,
    crossinline onFault: faultRequestCodeAction
) = CoroutineScope(Dispatchers.Main).launch {
    withContext(Dispatchers.Default) {
        this@completableRequest
    }.apply handleResponse@{
        if (status.isSuccess()) {
            onSuccess.invoke()
        } else {
            onFault.invoke(status.value)
        }
    }
}

inline fun <T: BaseEntity> HttpResponse.singleRequest(
    crossinline onSuccess: (T) -> Unit,
    crossinline onFault: faultRequestCodeAction
) = CoroutineScope(Dispatchers.Main).launch {
    withContext(Dispatchers.Default) {
        this@singleRequest
    }.apply handleResponse@{
        if (status.isSuccess()) {
           //onSuccess.invoke(this.content.)
        } else {
            onFault.invoke(status.value)
        }
    }
}