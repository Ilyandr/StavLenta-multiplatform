package gcu.production.stavlenta.android.repository.services

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import gcu.production.stavlenta.repository.model.TapeModel
import gcu.production.stavlenta.repository.rest.repository.ContentRepository
import java.lang.Exception


internal class TapesPagingSource(private val source: ContentRepository) : PagingSource<Int, TapeModel>() {

    override fun getRefreshKey(state: PagingState<Int, TapeModel>) = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TapeModel> =
        try {
            val nextPage = params.key ?: 0
            val tapesList = source.getContentPage(page = nextPage)

            LoadResult.Page(
                data = tapesList,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (tapesList.isEmpty()) null else nextPage + 1
            )
        } catch (ex: Exception) {
            Log.e("err", ex.stackTraceToString())

            LoadResult.Error(ex)
        }
}