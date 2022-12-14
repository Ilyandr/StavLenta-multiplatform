package gcu.production.stavlenta.android.presentation.screens.auth

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import gcu.production.stavlenta.android.R
import gcu.production.stavlenta.android.domain.models.auth.AuthModel
import gcu.production.stavlenta.android.domain.viewModels.auth.AuthViewModel
import gcu.production.stavlenta.android.presentation.views.other.BaseButton
import gcu.production.stavlenta.android.presentation.views.other.CustomDialog
import gcu.production.stavlenta.android.presentation.views.other.CustomOutlinedTextField
import gcu.production.stavlenta.android.presentation.views.other.HeaderTextView
import gcu.production.stavlenta.android.presentation.views.other.LinkTextView
import gcu.production.stavlenta.android.repository.source.Constants
import gcu.production.stavlenta.android.repository.source.Constants.REGISTRATION_DESTINATION
import gcu.production.stavlenta.repository.feature.other.SCREEN_IMAGE_URL
import ru.alexgladkov.odyssey.compose.RootController
import ru.alexgladkov.odyssey.compose.extensions.push
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.core.LaunchFlag


@Composable
internal fun AuthScreen(
    gifLoader: ImageLoader,
    viewModel: AuthViewModel = hiltViewModel(),
    rootController: RootController = LocalRootController.current
) {
    val state = viewModel.stateFlow.collectAsState()
    val scrollState = rememberScrollState()
    val loadingState = rememberSaveable  { mutableStateOf(false) }
    val (loginText, setLoginText) = rememberSaveable { mutableStateOf("") }
    val (passwordText, setPasswordText) = rememberSaveable { mutableStateOf("") }

    when (val value = state.value) {
        is AuthModel.LoadingState -> loadingState.value = true
        is AuthModel.SuccessState -> {
            loadingState.value = false
            rootController.push(Constants.HOME_DESTINATION, launchFlag = LaunchFlag.SingleNewTask)
            viewModel.actionReady()
        }
        is AuthModel.FaultState -> {
            CustomDialog(
                titleTextId = R.string.dialog_error_auth_title,
                descriptionTextId = value.messageId,
                iconId = R.drawable.ic_key,
                cancelAction = viewModel::actionReady
            )
            loadingState.value = false
        }
        else -> {}
    }

    Box(modifier = Modifier.fillMaxSize().scrollable(state = scrollState, orientation = Orientation.Vertical),
        contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = SCREEN_IMAGE_URL,
            imageLoader = gifLoader,
            contentScale = ContentScale.FillHeight,
            loading = {
                CircularProgressIndicator(
                    color = colorResource(id = R.color.primary),
                    modifier = Modifier.padding(horizontal = 128.dp, vertical = 32.dp)
                )
            },
            contentDescription = stringResource(R.string.auth_title)
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            colors = CardDefaults.cardColors(colorResource(id = R.color.card_color))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeaderTextView(R.string.auth_title)
            }

            Column(
                modifier = Modifier
                    .height(298.dp)
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomOutlinedTextField(
                    iconId = R.drawable.ic_email,
                    inputType = KeyboardType.Email,
                    labelTextId = R.string.mail,
                    loginText,
                    keyboardAction = ImeAction.Next,
                    setLoginText
                )

                Spacer(modifier = Modifier.height(6.dp))

                CustomOutlinedTextField(
                    iconId = R.drawable.ic_key,
                    inputType = KeyboardType.Password,
                    labelTextId = R.string.password,
                    passwordText,
                    keyboardAction = ImeAction.Done,
                    setPasswordText
                )

                LinkTextView(R.string.registration) {
                    rootController.push(REGISTRATION_DESTINATION)
                }

                Spacer(modifier = Modifier.height(16.dp))

                BaseButton(loadingState.value, R.string.log_in) {
                    viewModel.login(login = loginText, password = passwordText)
                }
            }
        }
    }
}