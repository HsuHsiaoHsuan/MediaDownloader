package idv.hsu.media.downloader.db

import androidx.room.*
import idv.hsu.media.downloader.db.relation.SearchAndInfo
import idv.hsu.media.downloader.vo.SearchRecord
import idv.hsu.media.downloader.vo.SearchType
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchRecordDao {

    @Transaction
    @Query("SELECT * FROM search_record_table WHERE search_type = :searchType ORDER BY search_time DESC")
    fun getSearchAndInfoByType(@SearchType searchType: Int): Flow<List<SearchAndInfo>>

    @Insert(entity = SearchRecord::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSearchRecord(data: SearchRecord): Long

    @Query("SELECT * FROM search_record_table WHERE url = :url")
    suspend fun getSearchRecord(url: String): SearchRecord?

    @Update
    suspend fun updateSearchRecord(record: SearchRecord)

    @Delete
    suspend fun deleteSearchRecord(record: SearchRecord)

    @Query("DELETE FROM search_record_table")
    suspend fun clear()
}