package gcu.production.stavlenta.android.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import dagger.hilt.android.AndroidEntryPoint
import gcu.production.stavlenta.android.presentation.screens.auth.AuthScreen
import gcu.production.stavlenta.android.presentation.screens.auth.RegistrationScreen
import gcu.production.stavlenta.android.presentation.screens.base.AddTapeScreen
import gcu.production.stavlenta.android.presentation.screens.base.HomeScreen
import gcu.production.stavlenta.android.repository.features.utils.requireGifImageLoader
import gcu.production.stavlenta.android.repository.source.Constants.ADD_TAPE_DESTINATION
import gcu.production.stavlenta.android.repository.source.Constants.AUTH_DESTINATION
import gcu.production.stavlenta.android.repository.source.Constants.HOME_DESTINATION
import gcu.production.stavlenta.android.repository.source.Constants.REGISTRATION_DESTINATION
import gcu.production.stavlenta.repository.di.CommonSDK
import gcu.production.stavlenta.repository.feature.other.LOGIN_KEY
import ru.alexgladkov.odyssey.compose.extensions.screen
import ru.alexgladkov.odyssey.compose.navigation.RootComposeBuilder
import ru.alexgladkov.odyssey.compose.setupNavigation
import javax.inject.Inject

@AndroidEntryPoint
internal class HostActivity @Inject constructor() : ComponentActivity() {

    private fun RootComposeBuilder.primaryScreen() {

        screen(AUTH_DESTINATION) {
            AuthScreen(requireGifImageLoader())
        }

        screen(REGISTRATION_DESTINATION) {
            RegistrationScreen(requireGifImageLoader())
        }

        screen(HOME_DESTINATION) {
            HomeScreen()
        }

        screen(ADD_TAPE_DESTINATION) {
            AddTapeScreen()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupNavigation(if (CommonSDK.storageSource.getString(LOGIN_KEY) == null) AUTH_DESTINATION else HOME_DESTINATION) {
            primaryScreen()
        }
    }
}