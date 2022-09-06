package idv.hsu.media.downloader.ui.download

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import idv.hsu.media.downloader.databinding.FragmentDownloadBinding
import idv.hsu.media.downloader.db.relation.DownloadAndInfo
import idv.hsu.media.downloader.vo.DOWNLOAD_STATE_DOWNLOADING
import idv.hsu.media.downloader.vo.DOWNLOAD_STATE_INIT
import idv.hsu.media.downloader.vo.DOWNLOAD_STATE_PARSING
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DownloadFragment : Fragment() {

    private var _binding: FragmentDownloadBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DownloadViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDownloadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pagerDownloadRecord.adapter =
            DownloadPagerAdapter(childFragmentManager, lifecycle)

        TabLayoutMediator(binding.tablayout, binding.pagerDownloadRecord) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Audio"
                }
                1 -> {
                    tab.text = "Video"
                }
                else -> {
                }
            }
        }.attach()

        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.downloadAudio.collectLatest {
                    showBadge(0, it)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.downloadVideo.collectLatest {
                    showBadge(1, it)
                }
            }
        }
    }

    private fun showBadge(tabPosition: Int, data: List<DownloadAndInfo>) {
        val count = data.count {
            it.download.downloadState == DOWNLOAD_STATE_INIT ||
                    it.download.downloadState == DOWNLOAD_STATE_PARSING ||
                    it.download.downloadState == DOWNLOAD_STATE_DOWNLOADING
        }
        val badge = binding.tablayout.getTabAt(tabPosition)?.orCreateBadge
        if (badge != null) {
            if (count > 0) {
                badge.number = count
            } else {
                binding.tablayout.getTabAt(tabPosition)?.removeBadge()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}