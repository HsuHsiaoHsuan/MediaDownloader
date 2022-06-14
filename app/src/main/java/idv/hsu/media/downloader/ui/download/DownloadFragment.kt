package idv.hsu.media.downloader.ui.download

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import idv.hsu.media.downloader.databinding.FragmentDownloadBinding

@AndroidEntryPoint
class DownloadFragment : Fragment() {

    private var _binding: FragmentDownloadBinding? = null
    private val binding get() = _binding!!


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

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}