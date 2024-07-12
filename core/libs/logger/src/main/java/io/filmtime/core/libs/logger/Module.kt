package io.filmtime.core.libs.logger

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal abstract class Module {

  @Singleton
  @Binds
  internal abstract fun bindsLogger(bind: AppLogger): Logger
}
