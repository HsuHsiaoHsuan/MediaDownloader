package idv.hsu.media.downloader.db

import androidx.room.Database
import androidx.room.RoomDatabase
import idv.hsu.media.downloader.vo.SearchRecord
import idv.hsu.media.downloader.vo.MyVideoInfo

@Database(entities = [SearchRecord::class, MyVideoInfo::class], version = 1, exportSchema = true)
abstract class MediaDatabase : RoomDatabase() {
    abstract fun SearchRecordDao(): SearchRecordDao
    abstract fun MyVideoInfoDao(): MyVideoInfoDao
}