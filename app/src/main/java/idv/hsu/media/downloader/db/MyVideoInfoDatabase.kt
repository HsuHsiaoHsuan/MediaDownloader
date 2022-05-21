package idv.hsu.media.downloader.db

import androidx.room.Database
import androidx.room.RoomDatabase
import idv.hsu.media.downloader.vo.MyVideoInfo

@Database(entities = [MyVideoInfo::class], version = 1, exportSchema = true)
abstract class MyVideoInfoDatabase : RoomDatabase() {
    abstract fun MyVideoInfoDao(): MyVideoInfoDao
}