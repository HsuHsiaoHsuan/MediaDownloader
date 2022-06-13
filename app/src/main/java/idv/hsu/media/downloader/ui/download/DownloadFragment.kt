package idv.hsu.media.downloader.ui.download

import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.view.menu.MenuBuilder
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import idv.hsu.media.downloader.R
import idv.hsu.media.downloader.databinding.FragmentDownloadBinding
import idv.hsu.media.downloader.db.relation.DownloadAndInfo
import idv.hsu.media.downloader.db.relation.SearchAndInfo
import idv.hsu.media.downloader.utils.downloadFolder
import idv.hsu.media.downloader.viewmodel.DownloadRecordViewModel
import idv.hsu.media.downloader.vo.DOWNLOAD_STATE_DONE
import idv.hsu.media.downloader.vo.DownloadState
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

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

    override fun onItemClick(data: DownloadAndInfo) {
    }

    override fun onActionClick(data: DownloadAndInfo, @DownloadState value: Int, view: View) {
        Timber.e("FREEMAN, state: $value data: ${data.myVideoInfo}")
        when(value) {
            DOWNLOAD_STATE_DONE -> {
                PopupMenu(requireActivity(), view).apply {
                    menuInflater.inflate(R.menu.menu_download_record, this.menu)
                    if (this.menu is MenuBuilder) {
                        val menuBuilder = this.menu as MenuBuilder
                        menuBuilder.setOptionalIconsVisible(true)
                    }
                    setOnMenuItemClickListener { item: MenuItem ->
                        if (item.itemId == R.id.menu_item_delete) {
                            val fileName = "${data.download.fileName}.${data.download.fileExtension}"
                            val file = File(requireActivity().downloadFolder(), fileName)

                            val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                            val contentResolver = requireActivity().contentResolver
                            val projection = MediaStore.Audio.Media.DATA + "=?"
                            val deleteRows = contentResolver.delete(uri, projection, arrayOf(file.absolutePath))
                            Timber.d("FREEMAN, deleteRows:$deleteRows")
                            file.deleteOnExit()
                            true
                        }
                        false
                    }
                }.show()
            }
            else -> {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}