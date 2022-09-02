package idv.hsu.media.downloader.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import idv.hsu.media.downloader.db.DownloadRecordDao
import idv.hsu.media.downloader.db.MediaDatabase
import idv.hsu.media.downloader.db.MyVideoInfoDao
import idv.hsu.media.downloader.db.SearchRecordDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideSearchRecordDao(database: MediaDatabase): SearchRecordDao {
        return database.SearchRecordDao()
    }

    @Provides
    fun provideMyVideoInfoDao(database: MediaDatabase): MyVideoInfoDao {
        return database.MyVideoInfoDao()
    }

    @Provides
    fun provideDownloadRecordDao(database: MediaDatabase): DownloadRecordDao {
        return database.DownloadRecordDao()
    }

    @Provides
    @Singleton
    fun provideMediaDatabase(@ApplicationContext context: Context): MediaDatabase {
        return Room.databaseBuilder(
            context,
            MediaDatabase::class.java,
            "media_db"
        ).build()
    }
}