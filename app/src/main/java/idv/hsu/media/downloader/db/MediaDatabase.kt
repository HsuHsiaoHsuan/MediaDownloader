package idv.hsu.media.downloader.db

import androidx.room.Database
import androidx.room.RoomDatabase
import idv.hsu.media.downloader.vo.DownloadRecord
import idv.hsu.media.downloader.vo.MyVideoInfo
import idv.hsu.media.downloader.vo.SearchRecord

@Database(
    entities = [SearchRecord::class, MyVideoInfo::class, DownloadRecord::class],
    version = 1,
    exportSchema = true
)
abstract class MediaDatabase : RoomDatabase() {
    abstract fun SearchRecordDao(): SearchRecordDao
    abstract fun MyVideoInfoDao(): MyVideoInfoDao
    abstract fun DownloadRecordDao(): DownloadRecordDao
}