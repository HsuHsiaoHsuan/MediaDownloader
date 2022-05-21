package idv.hsu.media.downloader.di

import android.content.Context
import androidx.work.WorkManager
import com.yausername.youtubedl_android.YoutubeDL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideYtdlp(): YoutubeDL = YoutubeDL.getInstance()

    @Provides
    @Singleton
    fun provideWorkerManager(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)
}