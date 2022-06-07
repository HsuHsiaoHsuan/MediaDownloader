package idv.hsu.media.downloader.vo

import android.os.Parcelable
import androidx.annotation.IntDef
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity(
    tableName = "download_record_table",
    primaryKeys = ["url", "file_name", "file_extension"]
)
data class DownloadRecord(
    val url: String,
    @ColumnInfo(name = "file_name")
    val fileName: String,
    @ColumnInfo(name = "file_extension")
    val fileExtension: String,
    @ColumnInfo(name = "download_time")
    val downloadTime: Long,
    @ColumnInfo(name = "download_progress")
    var downloadProgress: Float = 0f,
    @DownloadState
    @ColumnInfo(name = "download_state")
    var downloadState: Int = DOWNLOAD_STATE_INIT
) : Parcelable

@IntDef(
    DOWNLOAD_STATE_FAIL,
    DOWNLOAD_STATE_INIT,
    DOWNLOAD_STATE_DOWNLOADING,
    DOWNLOAD_STATE_DONE
)
@Retention(AnnotationRetention.SOURCE)
annotation class DownloadState

const val DOWNLOAD_STATE_FAIL = -1
const val DOWNLOAD_STATE_INIT = 0
const val DOWNLOAD_STATE_DOWNLOADING = 1
const val DOWNLOAD_STATE_DONE = 2