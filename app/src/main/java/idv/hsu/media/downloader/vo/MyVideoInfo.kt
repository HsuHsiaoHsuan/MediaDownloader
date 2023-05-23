package idv.hsu.media.downloader.vo

import android.os.Parcelable
import androidx.annotation.IntDef
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yausername.youtubedl_android.mapper.VideoInfo
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Keep
@Parcelize
@Serializable
@Entity(tableName = "video_info_table")
data class MyVideoInfo(
    @PrimaryKey(autoGenerate = false)
    val url: String,
    val thumbnail: String = "",
    val title: String = "",
    val uploader: String = "",
    val duration: Int = 0,
    @ConvertState
    @ColumnInfo(name = "convert_state")
    var convertState: Int = CONVERT_STATE_INIT
) : Parcelable

fun VideoInfo.toMyVideoInfo(userInputUrl: String): MyVideoInfo =
    MyVideoInfo(
        url = userInputUrl,
        thumbnail = this.thumbnail ?: "",
        title = this.title ?: "",
        uploader = this.uploader ?: "",
        duration = this.duration,
        convertState = CONVERT_STATE_DONE
    )

@IntDef(
    CONVERT_STATE_FAIL,
    CONVERT_STATE_INIT,
    CONVERT_STATE_CONVERTING,
    CONVERT_STATE_DONE
)
@Retention(AnnotationRetention.SOURCE)
annotation class ConvertState

const val CONVERT_STATE_FAIL = -1
const val CONVERT_STATE_INIT = 0
const val CONVERT_STATE_CONVERTING = 1
const val CONVERT_STATE_DONE = 2