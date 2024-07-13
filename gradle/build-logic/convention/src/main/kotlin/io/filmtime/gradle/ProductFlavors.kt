package io.filmtime.gradle

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor

enum class FlavorDimension {
  Store,
}

@Suppress("EnumEntryName")
enum class FilmTimeFlavor(val dimension: FlavorDimension) {
  googlePlay(FlavorDimension.Store),
  github(FlavorDimension.Store),
}

fun configureFlavors(
  commonExtension: CommonExtension<*, *, *, *, *, *>,
  configuration: ProductFlavor.(flavor: FilmTimeFlavor) -> Unit = {},
) {
  commonExtension.apply {
    flavorDimensions += FlavorDimension.Store.name
    productFlavors {
      FilmTimeFlavor.values().forEach {
        create(it.name) {
          dimension = it.dimension.name
          configuration(this, it)
        }
      }
    }
  }
}
