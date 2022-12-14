package gcu.production.stavlenta.android.domain.models.base

import androidx.annotation.StringRes
import gcu.production.stavlenta.repository.model.TapeModel


internal sealed class HomeModel {

    object DefaultState: HomeModel()

    object LoadingState: HomeModel()

    data class SuccessLoadState(val data: List<TapeModel>): HomeModel()

    data class FaultLoadState(@StringRes val messageId: Int): HomeModel()
}