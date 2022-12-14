package gcu.production.stavlenta.android.repository.source.architecture.viewModels

import kotlinx.coroutines.flow.StateFlow


internal interface InteractionViewModel <LiveDataType> {

    val stateFlow: StateFlow<LiveDataType>
    fun actionReady()
}