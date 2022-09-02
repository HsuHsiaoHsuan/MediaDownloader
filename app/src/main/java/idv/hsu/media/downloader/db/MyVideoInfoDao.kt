package idv.hsu.media.downloader.db

import androidx.room.*
import idv.hsu.media.downloader.vo.MyVideoInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface MyVideoInfoDao {
    @Query("SELECT * FROM video_info_table")
    fun getAllVideoInfo(): Flow<List<MyVideoInfo>>

    @Query("SELECT * FROM video_info_table WHERE url = :url")
    suspend fun getMyVideoInfo(url: String): MyVideoInfo?

    @Insert(entity = MyVideoInfo::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMyVideoInfo(data: MyVideoInfo)

    @Update
    suspend fun updateMyVideoInfo(data: MyVideoInfo)

    @Delete
    suspend fun delMyVideoInfo(data: MyVideoInfo)
}