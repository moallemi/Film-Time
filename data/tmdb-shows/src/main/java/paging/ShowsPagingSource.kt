package paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.filmtime.data.api.tmdb.TmdbShowsRemoteSource
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoThumbnail
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ShowsPagingSource @Inject constructor(
  private val tmdbShowsRemoteSource: TmdbShowsRemoteSource,
) : PagingSource<Int, VideoThumbnail>(){
  override fun getRefreshKey(state: PagingState<Int, VideoThumbnail>): Int? {
    return state.anchorPosition?.let {
      state.closestPageToPosition(it)?.prevKey?.plus(
        1
      ) ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
    }
  }

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VideoThumbnail> {
    val position = params.key?:1
    return try {
      delay(3000)
      val remoteData =
       tmdbShowsRemoteSource.getTrendingShows(position.toLong())
      when (remoteData) {
        is Result.Success -> {
          val data = remoteData.data
          val prevKey = if (position == 1) null else position - 1
          val nextKey = if (data.size < params.loadSize) null else position + 1
          LoadResult.Page(data, prevKey, nextKey)
        }
        is Result.Failure -> {
          val error = remoteData.error
          LoadResult.Error(mapGeneralErrorToException(error))
        }
      }

    }catch (e: IOException){
      LoadResult.Error(e)
    }catch (e: HttpException){
      LoadResult.Error(e)
    }

  }

  private fun mapGeneralErrorToException(error: GeneralError): Throwable {
    return when (error) {
      is GeneralError.ApiError -> Exception("API Error: ${error.message}, Code: ${error.code}")
      is GeneralError.NetworkError -> Exception("Network Error")
      is GeneralError.UnknownError -> error.error

    }
  }
}


