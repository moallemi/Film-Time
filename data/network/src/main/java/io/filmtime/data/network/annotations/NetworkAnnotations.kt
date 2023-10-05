package io.filmtime.data.network.annotations

import javax.inject.Qualifier

@Qualifier
@Target(
  AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER,
  AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.FIELD,
  AnnotationTarget.VALUE_PARAMETER
)
@Retention(AnnotationRetention.BINARY)
annotation class TraktNetwork
