package io.filmtime.data.network

import io.filmtime.data.network.adapter.NetworkResponse
import io.filmtime.data.network.model.CollectionResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface TmdbCollectionService {

  @GET("collection/{collection_id}")
  suspend fun getCollection(
    @Path("collection_id") id: Int,
  ): NetworkResponse<CollectionResponse, TmdbErrorResponse>
}
