package idv.hsu.media.downloader.ui.download

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class DownloadPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                DownloadCategoryFragment.newInstance(0)
            }
            1 -> {
                DownloadCategoryFragment.newInstance(1)
            }
            else -> {
                DownloadCategoryFragment.newInstance(2)
            }
        }
    }
}