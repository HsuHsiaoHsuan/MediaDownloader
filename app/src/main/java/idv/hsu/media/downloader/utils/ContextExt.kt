package idv.hsu.media.downloader.utils

import android.content.Context
import android.content.Intent
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.os.Environment
import android.util.TypedValue
import android.view.View
import androidx.annotation.MenuRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import idv.hsu.media.downloader.R
import idv.hsu.media.downloader.ui.browser.BrowserActivity
import java.io.File

fun Context.downloadFolder(): File {
    return File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        getString(R.string.app_name)
    )
}

const val EXTRA_URL = "url"
fun Context.openBrowser(url: String) {
    val intent = Intent(this, BrowserActivity::class.java).apply {
        putExtra(EXTRA_URL, url)
    }
    startActivity(intent)
}

private const val ICON_MARGIN = 4
fun Context.showPopupMenu(
    view: View,
    @MenuRes res: Int,
    listener: PopupMenu.OnMenuItemClickListener?
) {
    PopupMenu(this, view).apply {
        menuInflater.inflate(res, this.menu)
        if (this.menu is MenuBuilder) {
            val menuBuilder = this.menu as MenuBuilder
            menuBuilder.setOptionalIconsVisible(true)
            for (item in menuBuilder.visibleItems) {
                val iconMarginPx =
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        ICON_MARGIN.toFloat(),
                        resources.displayMetrics
                    )
                        .toInt()
                if (item.icon != null) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        item.icon =
                            InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0)
                    } else {
                        item.icon =
                            object :
                                InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0) {
                                override fun getIntrinsicWidth(): Int {
                                    return intrinsicHeight + iconMarginPx + iconMarginPx
                                }
                            }
                    }
                }
            }
            setOnMenuItemClickListener(listener)
        }
    }.show()
}