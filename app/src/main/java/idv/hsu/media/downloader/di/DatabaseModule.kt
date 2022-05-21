package idv.hsu.media.downloader.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import idv.hsu.media.downloader.db.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideSearchRecordDatabase(@ApplicationContext context: Context): SearchRecordDatabase {
        return Room.databaseBuilder(
            context,
            SearchRecordDatabase::class.java,
            "search_record"
        ).build()
    }

    @Provides
    fun provideSearchRecordDao(database: SearchRecordDatabase): SearchRecordDao {
        return database.SearchRecordDao()
    }

    @Provides
    @Singleton
    fun provideMyVideoInfoDatabase(@ApplicationContext context: Context): MyVideoInfoDatabase {
        return Room.databaseBuilder(
            context,
            MyVideoInfoDatabase::class.java,
            "my_video_info"
        ).build()
    }

    @Provides
    fun provideMyVideoInfoDao(database: MyVideoInfoDatabase): MyVideoInfoDao {
        return database.MyVideoInfoDao()
    }
}