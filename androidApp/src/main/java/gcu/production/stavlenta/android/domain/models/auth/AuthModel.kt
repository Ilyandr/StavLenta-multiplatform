package gcu.production.stavlenta.android.domain.models.auth

import androidx.annotation.StringRes
import gcu.production.stavlenta.repository.model.UserModel


internal sealed class AuthModel {

    object DefaultState: AuthModel()

    object LoadingState: AuthModel()

    data class FaultState(@StringRes val messageId: Int): AuthModel()

    data class SuccessState(val model: UserModel): AuthModel()
}