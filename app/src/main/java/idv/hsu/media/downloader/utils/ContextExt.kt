package idv.hsu.media.downloader.utils

import android.content.Context
import android.os.Environment
import idv.hsu.media.downloader.R
import java.io.File

fun Context.downloadFolder(): File {
    return File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        getString(R.string.app_name)
    )
}