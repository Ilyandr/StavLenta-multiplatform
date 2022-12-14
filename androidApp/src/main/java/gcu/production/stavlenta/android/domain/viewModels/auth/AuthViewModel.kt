package gcu.production.stavlenta.android.domain.viewModels.auth

import android.text.TextUtils
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gcu.production.stavlenta.android.R
import gcu.production.stavlenta.android.domain.models.auth.AuthModel
import gcu.production.stavlenta.android.repository.features.utils.FlowSupport.set
import gcu.production.stavlenta.android.repository.source.architecture.viewModels.FlowableViewModel
import gcu.production.stavlenta.repository.di.CommonSDK
import gcu.production.stavlenta.repository.feature.other.LOGIN_KEY
import gcu.production.stavlenta.repository.feature.other.PASSWORD_KEY
import gcu.production.stavlenta.repository.model.UserModel
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
internal class AuthViewModel @Inject constructor() : FlowableViewModel<AuthModel>() {

    private val restSource by lazy { CommonSDK.restAuthRepository }
    private val dataPreferences by lazy { CommonSDK.storageSource }
    private val mutableStateFlow by lazy { MutableStateFlow<AuthModel>(AuthModel.DefaultState) }
    override val stateFlow: StateFlow<AuthModel> by lazy { mutableStateFlow.asStateFlow() }


    init {
        this.stateFlow.onEach { currentState ->
            if (currentState is AuthModel.SuccessState) {
                with(dataPreferences) {
                    setValue(LOGIN_KEY, currentState.model.email)
                    setValue(PASSWORD_KEY, currentState.model.password)
                }
            }
        }.launchIn(viewModelScope)
    }

    override fun actionReady() {
        mutableStateFlow.set(AuthModel.DefaultState)
    }

    fun login(login: String, password: String) {

        mutableStateFlow.set(AuthModel.LoadingState)

        if (!TextUtils.isEmpty(login) && android.util.Patterns.EMAIL_ADDRESS.matcher(login)
                .matches() && password.isNotEmpty() && password.length >= 6
        ) {
            CoroutineScope(Dispatchers.Main).launch {
                restSource.login(login, password).let { response ->
                    mutableStateFlow.set(
                        if (response.isSuccess()) {
                            AuthModel.SuccessState(UserModel(email = login, password = password))
                        } else {
                            AuthModel.FaultState(R.string.authError)
                        }
                    )
                }
            }
        } else {
            mutableStateFlow.set(AuthModel.FaultState(R.string.incorrectDataError))
        }
    }
}