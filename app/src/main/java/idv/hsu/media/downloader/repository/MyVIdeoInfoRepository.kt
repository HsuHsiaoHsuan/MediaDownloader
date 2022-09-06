package idv.hsu.media.downloader.repository

import idv.hsu.media.downloader.db.MyVideoInfoDao
import idv.hsu.media.downloader.vo.MyVideoInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MyVideoInfoRepository @Inject constructor(
    private val repoMyVideoInfoDao: MyVideoInfoDao
) {
    val allMyVideoInfo: Flow<List<MyVideoInfo>> = repoMyVideoInfoDao.getAllVideoInfo()

    suspend fun getMyVideoInfo(url: String) = withContext(Dispatchers.IO) {
        repoMyVideoInfoDao.getMyVideoInfo(url)
    }

    suspend fun addMyVideoInfo(myVideoInfo: MyVideoInfo) = withContext(Dispatchers.IO) {
        repoMyVideoInfoDao.addMyVideoInfo(myVideoInfo)
    }

    suspend fun delMyVideoInfo(myVideoInfo: MyVideoInfo) = withContext(Dispatchers.IO) {
        repoMyVideoInfoDao.delMyVideoInfo(myVideoInfo)
    }
}