package idv.hsu.media.downloader.vo

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class SearchListItem(
    val url: String = "",
    val searchTime: Long = 0L,
    val title: String = "",
    val uploader: String = "",
    val duration: Int = 0
) : Parcelable