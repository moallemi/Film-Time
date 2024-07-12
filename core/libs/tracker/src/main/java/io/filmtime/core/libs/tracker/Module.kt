package io.filmtime.core.libs.tracker

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal abstract class TrackerBindsModule {

  @Singleton
  @Binds
  internal abstract fun bindsTracker(bind: AppFirebaseTracker): Tracker
}

@InstallIn(SingletonComponent::class)
@Module
internal object TrackerModuleProvider {

  @Provides
  @Singleton
  fun provideFirebaseAnalytics(): FirebaseAnalytics =
    Firebase.analytics

  @Provides
  @Singleton
  fun provideFirebaseCrashlytics(): FirebaseCrashlytics =
    FirebaseCrashlytics.getInstance()
}
