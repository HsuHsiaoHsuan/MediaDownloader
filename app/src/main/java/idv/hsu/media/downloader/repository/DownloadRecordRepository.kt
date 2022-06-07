package idv.hsu.media.downloader.repository

import idv.hsu.media.downloader.db.DownloadRecordDao
import idv.hsu.media.downloader.db.relation.DownloadAndInfo
import idv.hsu.media.downloader.vo.DownloadRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DownloadRecordRepository @Inject constructor(
    private val downloadRecordDao: DownloadRecordDao
) {
    val allDownloadRecord: Flow<List<DownloadAndInfo>> = downloadRecordDao.getAllDownloadAndInfo()

    suspend fun addDownloadRecord(data: DownloadRecord) = withContext(Dispatchers.IO) {
        downloadRecordDao.addDownloadRecord(data)
    }

    suspend fun updateDownloadRecord(data: DownloadRecord) = withContext(Dispatchers.IO) {
        downloadRecordDao.updateDownloadRecord(data)
    }

    suspend fun deleteDownloadRecord(data: DownloadRecord) = withContext(Dispatchers.IO) {
        downloadRecordDao.deleteDownloadRecord(data)
    }
}