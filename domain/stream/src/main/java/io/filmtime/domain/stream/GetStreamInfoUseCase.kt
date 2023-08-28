package io.filmtime.domain.stream

import io.filmtime.data.model.StreamInfo
import kotlinx.coroutines.flow.Flow

interface GetStreamInfoUseCase {

  suspend operator fun invoke(): Flow<StreamInfo>
}
