package idv.hsu.media.downloader.db

import androidx.room.Database
import androidx.room.RoomDatabase
import idv.hsu.media.downloader.po.SearchRecord

@Database(entities = [SearchRecord::class], version = 1, exportSchema = true)
abstract class SearchRecordDatabase : RoomDatabase() {
    abstract fun SearchRecordDao(): SearchRecordDao
}