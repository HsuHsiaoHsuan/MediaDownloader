package idv.hsu.media.downloader.vo

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yausername.youtubedl_android.mapper.VideoInfo
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity(tableName = "video_info_table")
data class MyVideoInfo(
    @PrimaryKey(autoGenerate = false)
    val url: String,
    val thumbnail: String = "",
    val title: String = "",
    val uploader: String = "",
    val duration: Int = 0
) : Parcelable

fun VideoInfo.toMyVideoInfo(userInputUrl: String): MyVideoInfo =
    MyVideoInfo(
        url = userInputUrl,
        thumbnail = this.thumbnail,
        title = this.title,
        uploader = this.uploader,
        duration = this.duration
    )