package io.filmtime.data.network.annotation

import javax.inject.Qualifier

@Qualifier
@Target(
  AnnotationTarget.FUNCTION,
  AnnotationTarget.PROPERTY_GETTER,
  AnnotationTarget.PROPERTY_SETTER,
  AnnotationTarget.FIELD,
  AnnotationTarget.VALUE_PARAMETER,
)
@Retention(AnnotationRetention.BINARY)
annotation class TraktNetwork


@Qualifier
@Target(
  AnnotationTarget.FUNCTION,
  AnnotationTarget.PROPERTY_GETTER,
  AnnotationTarget.PROPERTY_SETTER,
  AnnotationTarget.FIELD,
  AnnotationTarget.VALUE_PARAMETER,
)
@Retention(AnnotationRetention.BINARY)
annotation class TraktOkHttp

@Qualifier
@Target(
  AnnotationTarget.FUNCTION,
  AnnotationTarget.PROPERTY_GETTER,
  AnnotationTarget.PROPERTY_SETTER,
  AnnotationTarget.FIELD,
  AnnotationTarget.VALUE_PARAMETER,
)
@Retention(AnnotationRetention.BINARY)
annotation class TraktHeaderInterceptor
