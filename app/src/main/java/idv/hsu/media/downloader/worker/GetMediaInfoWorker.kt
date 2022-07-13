package idv.hsu.media.downloader.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.yausername.youtubedl_android.YoutubeDL
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import idv.hsu.media.downloader.db.MyVideoInfoDao
import idv.hsu.media.downloader.db.SearchRecordDao
import idv.hsu.media.downloader.vo.*
import idv.hsu.media.downloader.worker.DownloadMediaWorker.Companion.KEY_RESULT
import idv.hsu.media.downloader.worker.DownloadMediaWorker.Companion.KEY_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class GetMediaInfoWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted params: WorkerParameters,
    private val ytdlp: YoutubeDL,
    private val repoMyVideoInfoDao: MyVideoInfoDao,
    private val repoSearchRecordDao: SearchRecordDao
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val url = inputData.getString(KEY_URL)
            ?: return Result.failure(
                workDataOf(
                    KEY_RESULT to "url is null"
                )
            )

        val videoInfo = repoMyVideoInfoDao.getMyVideoInfo(url) ?: MyVideoInfo(url = url, convertState = CONVERT_STATE_CONVERTING)
        repoMyVideoInfoDao.addMyVideoInfo(videoInfo)
        // --------------
//        val searchRecord = repoSearchRecordDao.getSearchRecord(url)
//        if (searchRecord != null) {
//            searchRecord.convertState = CONVERT_STATE_CONVERTING
//            repoSearchRecordDao.updateSearchRecord(searchRecord)
//        }

        return withContext(Dispatchers.IO) {
            try {
                val videoInfo = ytdlp.getInfo(url)
                repoMyVideoInfoDao.addMyVideoInfo(videoInfo.toMyVideoInfo(url))
//                if (searchRecord != null) {
//                    searchRecord.convertState = CONVERT_STATE_DONE
//                    repoSearchRecordDao.updateSearchRecord(searchRecord)
//                }
                Result.success()
            } catch (e: Exception) {
                val videoInfo = MyVideoInfo(url = url, convertState = CONVERT_STATE_FAIL)
                repoMyVideoInfoDao.addMyVideoInfo(videoInfo)
                //------
//                if (searchRecord != null) {
//                    searchRecord.convertState = CONVERT_STATE_FAIL
//                    repoSearchRecordDao.updateSearchRecord(searchRecord)
//                }
                Result.failure(
                    workDataOf(
                        KEY_RESULT to e.toString()
                    )
                )
            }
        }
    }

    companion object {
        const val KEY_URL = "url"
        const val KEY_RESULT = "result"

        fun getInputData(
            url: String
        ): Data = workDataOf(
            KEY_URL to url
        )
    }
}