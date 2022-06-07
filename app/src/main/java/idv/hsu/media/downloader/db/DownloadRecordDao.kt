package idv.hsu.media.downloader.db

import androidx.room.*
import idv.hsu.media.downloader.db.relation.DownloadAndInfo
import idv.hsu.media.downloader.vo.DownloadRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadRecordDao {

    @Transaction
    @Query("SELECT * FROM download_record_table ORDER BY download_time DESC")
    fun getAllDownloadAndInfo(): Flow<List<DownloadAndInfo>>

    @Insert(entity = DownloadRecord::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDownloadRecord(data: DownloadRecord)

    @Update
    suspend fun updateDownloadRecord(data: DownloadRecord)

    @Delete
    suspend fun deleteDownloadRecord(data: DownloadRecord)
}