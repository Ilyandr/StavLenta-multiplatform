package gcu.production.stavlenta.android.repository.modules

import android.content.Context
import coil.request.CachePolicy
import coil.request.ImageRequest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import gcu.production.stavlenta.repository.di.CommonSDK
import gcu.production.stavlenta.repository.feature.other.LOGIN_KEY
import gcu.production.stavlenta.repository.feature.other.PASSWORD_KEY
import gcu.production.stavlenta.repository.feature.other.convertToBase64


@Module
@InstallIn(ViewModelComponent::class)
internal class RestModule {

    @Provides
    fun requireAuthImageRequest(@ApplicationContext context: Context) =
        ImageRequest.Builder(context)
            .addHeader("Authorization", CommonSDK.storageSource.run {
                convertToBase64(getString(LOGIN_KEY), getString(PASSWORD_KEY))
            })
            .crossfade(true)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.DISABLED)
}