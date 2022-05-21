package idv.hsu.media.downloader.vo

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class MyVideoFormat(
    val formatId: String = "",
    val format: String = "",
    val width: Int = 0,
    val height: Int = 0,
    val asr: Int = 0,
    val fps: Float = 0F,
    val fileSize: Long = 0L,
    val url: String? = "",
    val manifestUrl: String? = "",
    val ext: String = ""
) : Parcelable