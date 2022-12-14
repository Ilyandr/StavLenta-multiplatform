package gcu.production.stavlenta.android.presentation.views.tapes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import gcu.production.stavlenta.android.R
import gcu.production.stavlenta.android.presentation.views.other.EmptyView
import gcu.production.stavlenta.android.repository.features.utils.isNetworkAvailable
import gcu.production.stavlenta.repository.feature.other.API_IMAGE_HOST
import gcu.production.stavlenta.repository.model.TapeModel
import kotlinx.coroutines.flow.Flow


@Composable
internal fun TapeView(model: TapeModel) {

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color = colorResource(id = R.color.card_tape_color))
    ) {
        Text(
            text = model.name!!,
            textAlign = TextAlign.Start,
            fontStyle = FontStyle(R.font.sf_heavy),
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.black),
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        )

        Text(
            text = model.body,
            textAlign = TextAlign.Start,
            fontStyle = FontStyle(R.font.sf_regular),
            fontSize = 17.sp,
            color = colorResource(id = R.color.black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(bottom = 8.dp)
        )
    }
}

@Composable
internal fun BaseTapesListView(tapesList: Flow<PagingData<TapeModel>>, imageRequest: ImageRequest.Builder) {

    val tapesListItems: LazyPagingItems<TapeModel> = tapesList.collectAsLazyPagingItems()
    val loadingState = rememberSaveable { mutableStateOf(true) }
    val errorState = rememberSaveable { mutableStateOf(false) }
    val emptyState = rememberSaveable { mutableStateOf(false) }
    val refreshState = rememberSwipeRefreshState(isRefreshing = false)
    val isNetworkAvailable = isNetworkAvailable(LocalContext.current)


    SwipeRefresh(state = refreshState, onRefresh = {
        tapesListItems.refresh()
    }) {
        if (loadingState.value) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    Modifier
                        .height(32.dp)
                        .width(32.dp), colorResource(id = R.color.primary)
                )
            }
        }
        else if (emptyState.value && isNetworkAvailable) {
            EmptyView(
                primaryColor = R.color.light_gray,
                iconId = R.drawable.ic_empty,
                textId = R.string.empty_list
            )
        }
        else if (errorState.value || !isNetworkAvailable) {
            tapesListItems.retry()
            EmptyView(
                primaryColor = R.color.error_color,
                iconId = R.drawable.ic_error,
                textId = R.string.error_list
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = tapesListItems) { singleItem ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    TapeView(model = singleItem!!)

                    singleItem.images?.run {
                        if (isEmpty()) return@run

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            for (index in indices) {
                                if (index > 3) return@run
                                AsyncImage(
                                    modifier = Modifier.weight(1f),
                                    model = imageRequest.data("$API_IMAGE_HOST${get(index)}").build(),
                                    placeholder = painterResource(id = R.drawable.ic_default_placeholder),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = ""
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    tapesListItems.apply {
        when {
            loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading -> {
                loadingState.value = true
                emptyState.value = false
                errorState.value = false
            }

            loadState.append is LoadState.Error -> {
                errorState.value = true
                loadingState.value = false
            }

            loadState.append is LoadState.NotLoading -> {
                emptyState.value = true
                loadingState.value = false
            }
        }
    }
}

