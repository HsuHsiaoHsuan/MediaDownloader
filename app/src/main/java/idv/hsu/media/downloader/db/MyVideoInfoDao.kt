package idv.hsu.media.downloader.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import idv.hsu.media.downloader.vo.MyVideoInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface MyVideoInfoDao {
    @Query("SELECT * FROM video_info_table")
    fun getAllVideoInfo(): Flow<List<MyVideoInfo>>

    @Query("SELECT * FROM video_info_table WHERE original_url = :url")
    suspend fun getMyVideoInfo(url: String): MyVideoInfo

    @Insert(entity = MyVideoInfo::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMyVideoInfo(data: MyVideoInfo)
}