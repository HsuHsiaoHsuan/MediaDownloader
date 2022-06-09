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
import dagger.hilt.android.AndroidEntryPoint
import idv.hsu.media.downloader.databinding.FragmentDownloadBinding
import idv.hsu.media.downloader.db.relation.SearchAndInfo
import idv.hsu.media.downloader.viewmodel.DownloadRecordViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class DownloadFragment : Fragment(), DownloadAdapter.OnDownloadRecordClickListener {

    private var _binding: FragmentDownloadBinding? = null
    private val binding get() = _binding!!

    private val downloadRecordViewModel: DownloadRecordViewModel by viewModels()

    private val adapter = DownloadAdapter().apply {
        listener = this@DownloadFragment
    }

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
        with(binding.listDownloadRecord) {
            adapter = this@DownloadFragment.adapter
            setHasFixedSize(true)
        }
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                downloadRecordViewModel.allDownloadRecord.collect {
                    adapter.setData(it)
                }
            }
        }
    }

    override fun onItemClick(data: SearchAndInfo) {
    }

    override fun onActionClick(data: SearchAndInfo) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}