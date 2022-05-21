package idv.hsu.media.downloader.vo

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yausername.youtubedl_android.mapper.VideoInfo
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity(tableName = "video_info_table")
data class MyVideoInfo(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "original_url")
    val originalUrl: String,
    val title: String = "",
    val uploader: String = "",
    val duration: Int = 0,
    val description: String = "",
) : Parcelable

fun VideoInfo.toMyVideoInfo(): MyVideoInfo =
    MyVideoInfo(
        originalUrl = this.webpageUrl,
        title = this.title,
        uploader = this.uploader,
        duration = this.duration,
        description = this.description
    )