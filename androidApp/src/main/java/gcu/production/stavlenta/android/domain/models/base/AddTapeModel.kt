package gcu.production.stavlenta.android.domain.models.base

import androidx.annotation.StringRes


internal sealed class AddTapeModel {

    object DefaultState : AddTapeModel()

    object LoadingState : AddTapeModel()

    object SuccessState : AddTapeModel()

    data class FaultGlobalState(@StringRes val errorId: Int) : AddTapeModel()

    data class FaultLocaleState(@StringRes val errorId: Int, val type: AddTapeViewType) : AddTapeModel()
}

internal enum class AddTapeViewType {
    DESCRIPTION_TYPE, HEADER_TYPE
}