package idv.hsu.media.downloader.db

import androidx.room.*
import idv.hsu.media.downloader.db.relation.SearchAndInfo
import idv.hsu.media.downloader.po.SearchRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchRecordDao {

    @Query("SELECT * FROM search_record_table")
    fun getAllSearchRecord(): Flow<List<SearchRecord>>

    @Insert(entity = SearchRecord::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSearchRecord(data: SearchRecord): Long

    @Delete
    suspend fun deleteSearchRecord(record: SearchRecord)

    @Query("DELETE FROM search_record_table")
    suspend fun clear()

    @Transaction
    @Query("SELECT * FROM search_record_table ORDER BY search_time DESC")
    fun getAllSearchAndInfo(): Flow<List<SearchAndInfo>>
}