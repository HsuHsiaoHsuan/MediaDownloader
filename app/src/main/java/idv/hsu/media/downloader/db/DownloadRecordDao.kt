package idv.hsu.media.downloader.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import idv.hsu.media.downloader.db.relation.DownloadAndInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadRecordDao {

    @Transaction
    @Query("SELECT * FROM download_record_table ORDER BY download_time DESC")
    fun getAllDownloadAndInfo(): Flow<List<DownloadAndInfo>>
}