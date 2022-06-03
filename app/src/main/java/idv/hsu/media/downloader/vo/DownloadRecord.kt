package idv.hsu.media.downloader.vo

import android.os.Parcelable
import androidx.annotation.IntDef
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity(tableName = "download_record_table")
data class DownloadRecord(
    @PrimaryKey(autoGenerate = false)
    val url: String,
    @ColumnInfo(name = "download_time")
    val downloadTime: Long,
    @DownloadState
    @ColumnInfo(name = "download_state")
    val downloadState: Int = DOWNLOAD_STATE_INIT
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