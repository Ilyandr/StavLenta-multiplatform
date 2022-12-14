package gcu.production.stavlenta.android.domain.viewModels.base

import dagger.hilt.android.lifecycle.HiltViewModel
import gcu.production.stavlenta.android.R
import gcu.production.stavlenta.android.domain.models.base.AddTapeModel
import gcu.production.stavlenta.android.domain.models.base.AddTapeViewType
import gcu.production.stavlenta.android.repository.features.utils.FlowSupport.set
import gcu.production.stavlenta.android.repository.source.architecture.viewModels.FlowableViewModel
import gcu.production.stavlenta.repository.di.CommonSDK
import gcu.production.stavlenta.repository.model.AddTapeEntity
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
internal class AddTapeViewModel @Inject constructor() : FlowableViewModel<AddTapeModel>() {

    private val contentSource by lazy { CommonSDK.restContentRepository }
    private val mutableStateFlow: MutableStateFlow<AddTapeModel> by lazy {
        MutableStateFlow(
            AddTapeModel.DefaultState
        )
    }
    override val stateFlow: StateFlow<AddTapeModel> by lazy { mutableStateFlow.asStateFlow() }


    override fun actionReady() {
        mutableStateFlow.set(AddTapeModel.DefaultState)
    }

    fun addTape(model: AddTapeEntity, files: List<File>) {

        if (model.name.isEmpty()) {
            mutableStateFlow.set(
                AddTapeModel.FaultLocaleState(
                    R.string.headerInputError,
                    AddTapeViewType.HEADER_TYPE
                )
            )
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            contentSource.addContent(body = AddTapeEntity(
                name = model.name,
                body = model.body,
                type = model.type,
                sourceUrl = model.sourceUrl
            ),
                content = mutableListOf<ByteArray>().apply {
                    files.forEach { singleFile -> add(singleFile.readBytes()) }
                }
            ).apply {
                files.forEach { singleFile -> singleFile.deleteRecursively() }
                mutableStateFlow.set(
                    if (status.isSuccess()) AddTapeModel.SuccessState else AddTapeModel.FaultGlobalState(
                        R.string.error_add_tape
                    )
                )
            }
        }
    }
}