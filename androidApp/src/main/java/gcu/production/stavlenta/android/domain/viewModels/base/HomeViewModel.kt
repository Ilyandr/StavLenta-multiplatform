package gcu.production.stavlenta.android.domain.viewModels.base

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import coil.request.ImageRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import gcu.production.stavlenta.android.domain.models.base.HomeModel
import gcu.production.stavlenta.android.repository.features.utils.FlowSupport.set
import gcu.production.stavlenta.android.repository.services.TapesPagingSource
import gcu.production.stavlenta.android.repository.source.architecture.viewModels.FlowableViewModel
import gcu.production.stavlenta.repository.di.CommonSDK
import gcu.production.stavlenta.repository.model.TapeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor() : FlowableViewModel<HomeModel>() {

    @Inject
    lateinit var imageRequest: ImageRequest.Builder

    private val contentSource by lazy { CommonSDK.restContentRepository }
    private val mutableStateFlow by lazy { MutableStateFlow(HomeModel.DefaultState) }
    override val stateFlow: StateFlow<HomeModel> by lazy { mutableStateFlow.asStateFlow() }

    val tapesFlowModel: Flow<PagingData<TapeModel>> = Pager(PagingConfig(pageSize = 10)) {
        TapesPagingSource(contentSource)
    }.flow.cachedIn(viewModelScope)


    override fun actionReady() {
        mutableStateFlow.set(HomeModel.DefaultState)
    }
}