package gcu.production.stavlenta.android.presentation.views.other

import android.annotation.SuppressLint
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import gcu.production.stavlenta.android.R
import gcu.production.stavlenta.repository.feature.other.unitAction
import ru.alexgladkov.odyssey.compose.RootController
import ru.alexgladkov.odyssey.compose.local.LocalRootController


@Composable
internal fun HeaderTextView(@StringRes textId: Int) {
    Text(
        text = stringResource(textId, "World"),
        color = Color.Black,
        textAlign = TextAlign.Center,
        fontSize = 21.sp,
        fontStyle = FontStyle(R.font.sf_medium)
    )
}

@Composable
internal inline fun LinkTextView(@StringRes textId: Int, crossinline clickAction: unitAction) {
    Button(
        onClick = { clickAction.invoke() },
        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.transparent))
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = stringResource(textId, "World"),
            color = Color.Black,
            textAlign = TextAlign.Start,
            fontSize = 17.sp,
            fontStyle = FontStyle(R.font.sf_regular),
            textDecoration = TextDecoration.Underline
        )
    }
}

@Composable
internal fun CustomDialog(
    openDialogCustom: MutableState<Boolean>? = null,
    @StringRes titleTextId: Int? = null,
    @StringRes descriptionTextId: Int,
    @DrawableRes iconId: Int? = null,
    positiveButton: unitAction? = null,
    negativeButton: unitAction? = null,
    cancelAction: unitAction
) {
    Dialog(onDismissRequest = {
        openDialogCustom?.value = false
        cancelAction.invoke()
    }) {

        val primaryColor = colorResource(id = R.color.primary)

        Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {

            Column(Modifier.background(Color.White)) {

                iconId?.run {
                    Image(
                        painter = painterResource(id = this),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        colorFilter = ColorFilter.tint(color = primaryColor),
                        modifier = Modifier
                            .padding(top = 35.dp)
                            .height(70.dp)
                            .fillMaxWidth(),
                    )
                }

                Column(modifier = Modifier.padding(16.dp)) {

                    titleTextId?.run {
                        Text(
                            text = stringResource(id = this),
                            textAlign = TextAlign.Center,
                            fontStyle = FontStyle(R.font.sf_heavy),
                            fontSize = 24.sp,
                            modifier = Modifier
                                .padding(top = 6.dp)
                                .fillMaxWidth(),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Text(
                        text = stringResource(id = descriptionTextId),
                        textAlign = TextAlign.Center,
                        fontStyle = FontStyle(R.font.sf_regular),
                        fontSize = 19.sp,
                        modifier = Modifier
                            .padding(top = 12.dp, start = 24.dp, end = 24.dp)
                            .fillMaxWidth(),
                    )
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .background(Color.LightGray),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    negativeButton?.run {
                        androidx.compose.material3.TextButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                openDialogCustom?.value = false
                                invoke()
                                cancelAction.invoke()
                            }) {
                            Text(
                                stringResource(R.string.dialog_back),
                                fontStyle = FontStyle(R.font.sf_heavy),
                                fontSize = 18.sp,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(top = 6.dp, bottom = 6.dp)
                            )
                        }
                    }

                    androidx.compose.material3.TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            openDialogCustom?.value = false
                            positiveButton?.invoke()
                            cancelAction.invoke()
                        }) {
                        Text(
                            stringResource(R.string.dialog_next),
                            fontStyle = FontStyle(R.font.sf_heavy),
                            fontSize = 18.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(top = 6.dp, bottom = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
internal inline fun AppBarLayout(
    rootController: RootController = LocalRootController.current,
    @StringRes titleId: Int? = null,
    enableBackButton: Boolean,
    transparentBar: Boolean,
    crossinline menuContent: @Composable unitAction = {},
    crossinline content: @Composable unitAction
) {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            if (transparentBar)
                return@Scaffold

            TopAppBar(title = {
                if (enableBackButton)
                    IconButton(onClick = { rootController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = null,
                            tint = colorResource(id = R.color.white),
                        )
                    }

                titleId?.run {
                    Text(
                        text = stringResource(id = titleId),
                        textAlign = TextAlign.Start,
                        fontStyle = FontStyle(R.font.sf_heavy),
                        fontSize = 23.sp,
                        color = Color.White,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }, actions = { menuContent.invoke() }, backgroundColor = colorResource(id = R.color.primary))
        },
        content = {
            if (!transparentBar) {
                content.invoke()
                return@Scaffold
            }
            Box {
                content.invoke()
                TopAppBar(title = {
                    if (enableBackButton)
                        IconButton(onClick = { rootController.popBackStack() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_back),
                                contentDescription = null,
                                tint = colorResource(id = R.color.primary),
                            )
                        }

                    titleId?.run {
                        Text(
                            text = stringResource(id = this),
                            textAlign = TextAlign.Center,
                            fontStyle = FontStyle(R.font.sf_heavy),
                            fontSize = 24.sp,
                            modifier = Modifier
                                .padding(top = 6.dp)
                                .fillMaxWidth(),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }, backgroundColor = Color.Transparent.copy(alpha = 0.0f))
            }
        }
    )
}

@Composable
internal fun EmptyView(@ColorRes primaryColor: Int, @DrawableRes iconId: Int, @StringRes textId: Int) {

    val primaryColorResource = colorResource(id = primaryColor)

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        Icon(painter = painterResource(id = iconId), contentDescription = null, tint = primaryColorResource)

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(id = textId),
            textAlign = TextAlign.Center,
            color = primaryColorResource,
            fontStyle = FontStyle(R.font.sf_medium),
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            overflow = TextOverflow.Ellipsis
        )
    }
}