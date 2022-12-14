package gcu.production.stavlenta.android.presentation.screens.base

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import gcu.production.stavlenta.android.domain.models.base.HomeModel
import gcu.production.stavlenta.android.domain.viewModels.base.HomeViewModel
import gcu.production.stavlenta.android.presentation.views.tapes.BaseTapesListView
import gcu.production.stavlenta.android.R
import gcu.production.stavlenta.android.repository.source.Constants.ADD_TAPE_DESTINATION
import ru.alexgladkov.odyssey.compose.RootController
import ru.alexgladkov.odyssey.compose.extensions.push
import ru.alexgladkov.odyssey.compose.local.LocalRootController


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
internal fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    rootController: RootController = LocalRootController.current
) {
    val viewModelState = viewModel.stateFlow.collectAsState()
    val scaffoldState = rememberScaffoldState()

    val primaryColor = colorResource(id = R.color.primary)
    val whiteColor = colorResource(id = R.color.white)

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = {
                Text(
                    text = stringResource(id = R.string.home_title),
                    textAlign = TextAlign.Start,
                    fontStyle = FontStyle(R.font.sf_heavy),
                    fontSize = 23.sp,
                    color = whiteColor,
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }, backgroundColor = primaryColor)
        }, floatingActionButton = {
            FloatingActionButton(backgroundColor = primaryColor, onClick = {
                rootController.push(ADD_TAPE_DESTINATION)
            },
                content = {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = Icons.Rounded.Add,
                        tint = whiteColor, contentDescription = ""
                    )
                }
            )
        }) {

        when (viewModelState.value) {

            is HomeModel.SuccessLoadState -> {
                viewModel.actionReady()
            }
            else -> {}
        }

        BaseTapesListView(viewModel.tapesFlowModel, viewModel.imageRequest)
    }
}