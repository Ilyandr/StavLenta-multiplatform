package gcu.production.stavlenta.android.presentation.screens.base

import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import gcu.production.stavlenta.android.domain.models.base.AddTapeModel
import gcu.production.stavlenta.android.domain.viewModels.base.AddTapeViewModel
import gcu.production.stavlenta.android.presentation.views.other.AppBarLayout
import gcu.production.stavlenta.android.presentation.views.other.CustomDialog
import gcu.production.stavlenta.android.presentation.views.other.CustomEditText
import gcu.production.stavlenta.android.repository.features.utils.PhotoHelper.compressGetImageFilePath
import gcu.production.stavlenta.android.repository.features.utils.extractBitmap
import gcu.production.stavlenta.android.R
import gcu.production.stavlenta.repository.model.AddTapeEntity
import ru.alexgladkov.odyssey.compose.RootController
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import java.io.File


@Composable
internal fun AddTapeScreen(
    viewModel: AddTapeViewModel = hiltViewModel(),
    rootController: RootController = LocalRootController.current
) {
    val state = viewModel.stateFlow.collectAsState()
    val context = LocalContext.current
    val (headerText, setHeaderText) = rememberSaveable { mutableStateOf("") }
    val (descriptionText, setDescriptionText) = rememberSaveable { mutableStateOf("") }
    val loadingState = rememberSaveable { mutableStateOf(false) }
    val lightGrayColor = colorResource(id = R.color.light_gray)

    val bitmapList = remember { mutableStateListOf<Bitmap>() }
    val uriList = remember { mutableStateListOf<Uri?>() }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (!uriList.contains(uri)) {
                uriList.add(uri)
                (uri extractBitmap context)?.let { bitmapList.add(it) }
            }
        }

    when (val value = state.value) {

        is AddTapeModel.LoadingState -> {
            loadingState.value = true
        }
        is AddTapeModel.SuccessState -> {
            loadingState.value = false
            rootController.popBackStack()
            viewModel.actionReady()
        }
        is AddTapeModel.FaultLocaleState -> {
            CustomDialog(
                titleTextId = R.string.dialog_error_auth_title,
                descriptionTextId = value.errorId,
                iconId = R.drawable.ic_error,
                cancelAction = viewModel::actionReady
            )
            loadingState.value = false
        }
        is AddTapeModel.FaultGlobalState -> {
            Toast.makeText(LocalContext.current, value.errorId, Toast.LENGTH_SHORT).show()
        }
        else -> {}
    }

    AppBarLayout(
        titleId = R.string.title_add_tape,
        enableBackButton = true,
        transparentBar = false,
        menuContent = {
            IconButton(onClick = {
                viewModel.addTape(
                    AddTapeEntity(
                        name = headerText,
                        body = descriptionText,
                    ),
                    mutableListOf<File>().apply {
                        uriList.forEach { singleUri ->
                            if (singleUri != null) {
                                add(
                                    File(
                                        compressGetImageFilePath(
                                            context,
                                            singleUri,
                                            "folder"
                                        )
                                    )
                                )
                            }
                        }
                    }
                )
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_done),
                    tint = colorResource(id = R.color.white),
                    contentDescription = null
                )
            }
        }) {
        ConstraintLayout(
            requireAddTapeConstrains(), modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (bitmapList.isNotEmpty())
                LazyRow(
                    modifier = Modifier.layoutId("imagesList"),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    content = {
                        items(items = bitmapList) { singleImageBitmap ->
                            AsyncImage(
                                modifier = Modifier
                                    .size(128.dp)
                                    .clip(RoundedCornerShape(8))
                                    .border(1.dp, Color.LightGray, RoundedCornerShape(8)),
                                model = singleImageBitmap,
                                contentScale = ContentScale.Crop,
                                contentDescription = null
                            )
                        }
                    })

            CustomEditText(
                modifier = Modifier
                    .layoutId("headerView")
                    .padding(top = 12.dp),
                labelTextId = R.string.add_tape_header,
                value = headerText,
                onValueChange = setHeaderText
            )

            Divider(
                color = lightGrayColor,
                thickness = 0.5.dp,
                modifier = Modifier.layoutId("dividerFirstView")
            )

            CustomEditText(
                modifier = Modifier
                    .layoutId("descriptionView")
                    .padding(top = 12.dp),
                labelTextId = R.string.add_tape_description,
                value = descriptionText,
                onValueChange = setDescriptionText
            )

            Divider(
                color = lightGrayColor,
                thickness = 0.5.dp,
                modifier = Modifier.layoutId("dividerSecondView")
            )

            Row(
                modifier = Modifier.layoutId("bottomContainer"),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = { launcher.launch("image/*") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_image),
                        tint = colorResource(id = R.color.light_gray),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

internal fun requireAddTapeConstrains() = ConstraintSet {

    with(createRefFor("imagesList")) imagesList@{

        with(createRefFor("headerView")) headerView@{

            with(createRefFor("dividerFirstView")) dividerFirstView@{

                with(createRefFor("descriptionView")) descriptionView@{

                    with(createRefFor("dividerSecondView")) dividerSecondView@{

                        with(createRefFor("bottomContainer")) bottomContainer@{

                            constrain(this@imagesList) {
                                top.linkTo(parent.top, margin = 16.dp)
                                start.linkTo(parent.start, margin = 6.dp)
                                end.linkTo(parent.end, margin = 6.dp)
                                width = Dimension.matchParent
                                height = Dimension.value(136.dp)
                            }

                            constrain(this@headerView) {
                                top.linkTo(this@imagesList.bottom, margin = 8.dp)
                                start.linkTo(parent.start, margin = 6.dp)
                                end.linkTo(parent.end, margin = 6.dp)
                                width = Dimension.matchParent
                                height = Dimension.value(64.dp)
                            }

                            constrain(this@dividerFirstView) {
                                top.linkTo(this@headerView.bottom, margin = 8.dp)
                                start.linkTo(parent.start, margin = 16.dp)
                                end.linkTo(parent.end, margin = 16.dp)
                                width = Dimension.matchParent
                                height = Dimension.value(0.5.dp)
                            }

                            constrain(this@descriptionView) {
                                top.linkTo(this@dividerFirstView.bottom, margin = 8.dp)
                                bottom.linkTo(this@dividerSecondView.top, margin = 8.dp)
                                start.linkTo(parent.start, margin = 6.dp)
                                end.linkTo(parent.end, margin = 6.dp)
                                width = Dimension.matchParent
                                height = Dimension.fillToConstraints
                            }

                            constrain(this@dividerSecondView) {
                                bottom.linkTo(this@bottomContainer.top, margin = 8.dp)
                                start.linkTo(parent.start, margin = 16.dp)
                                end.linkTo(parent.end, margin = 16.dp)
                                width = Dimension.matchParent
                                height = Dimension.value(0.5.dp)
                            }

                            constrain(this@bottomContainer) {
                                top.linkTo(this@dividerSecondView.top, margin = 8.dp)
                                start.linkTo(parent.start, margin = 6.dp)
                                end.linkTo(parent.end, margin = 6.dp)
                                bottom.linkTo(parent.bottom, margin = 8.dp)
                                width = Dimension.matchParent
                            }
                        }
                    }
                }
            }
        }
    }
}

