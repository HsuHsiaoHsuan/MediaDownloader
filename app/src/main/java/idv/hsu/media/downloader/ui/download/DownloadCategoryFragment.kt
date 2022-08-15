package idv.hsu.media.downloader.ui.download

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yausername.youtubedl_android.YoutubeDL
import dagger.hilt.android.AndroidEntryPoint
import idv.hsu.media.downloader.R
import idv.hsu.media.downloader.databinding.FragmentDownloadCategoryBinding
import idv.hsu.media.downloader.db.relation.DownloadAndInfo
import idv.hsu.media.downloader.utils.downloadFolder
import idv.hsu.media.downloader.utils.showPopupMenu
import idv.hsu.media.downloader.viewmodel.DownloadRecordViewModel
import idv.hsu.media.downloader.viewmodel.GetMediaViewModel
import idv.hsu.media.downloader.viewmodel.ParseMediaViewModel
import idv.hsu.media.downloader.vo.*
import idv.hsu.media.downloader.worker.MEDIA_TYPE_AUDIO
import idv.hsu.media.downloader.worker.MEDIA_TYPE_VIDEO
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

@AndroidEntryPoint
class DownloadCategoryFragment : Fragment(), DownloadAdapter.OnDownloadRecordClickListener {

    private var _binding: FragmentDownloadCategoryBinding? = null
    private val binding get() = _binding!!

    private val downloadRecordViewModel: DownloadRecordViewModel by viewModels()
    private val parseMediaViewModel: ParseMediaViewModel by viewModels()
    private val getMediaViewModel: GetMediaViewModel by viewModels()

    private val adapter = DownloadAdapter().apply {
        listener = this@DownloadCategoryFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDownloadCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.listDownloadRecord) {
            adapter = this@DownloadCategoryFragment.adapter
            setHasFixedSize(true)
        }
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                when (arguments?.getInt(KEY_MODE, 2)) {
                    0 -> {
                        downloadRecordViewModel.allAudioRecord.collect {
                            adapter.setData(it)
                        }
                    }
                    1 -> {
                        downloadRecordViewModel.allVideoRecord.collect {
                            adapter.setData(it)
                        }
                    }
                    else -> {
                        downloadRecordViewModel.allDownloadRecord.collect {
                            adapter.setData(it)
                        }
                    }
                }
            }
        }
    }

    override fun refreshVideoInfo(url: String) {
        parseMediaViewModel.getVideoInfo(url)
    }

    override fun onItemClick(data: DownloadAndInfo) {
        Timber.e("FREEMAN, onItemClick: $data")
        val file = File(requireActivity().downloadFolder(), "${data.download.fileName}.${data.download.fileExtension}")
        Timber.e("FREEMAN, exist?: ${file.exists()}")
        if (file.exists()) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(file.absolutePath))
            val mimeType = when (data.download.getMediaType()) {
                MEDIA_TYPE_AUDIO -> {
                    "audio/mpeg"
                }
                MEDIA_TYPE_VIDEO -> {
                    "video/mp4"
                }
                else -> {
                    ""
                }
            }
            intent.setDataAndType(Uri.parse(file.absolutePath), mimeType)
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                requireActivity().startActivity(intent)
            } else {
                Toast.makeText(requireActivity(), "No App can play this file", Toast.LENGTH_SHORT).show()
            }
        } else {
            Timber.e("FREEMAN, file not exist")
        }
    }

    override fun onActionClick(data: DownloadAndInfo, @DownloadState value: Int, view: View) {
        Timber.e("FREEMAN, state: $value data: ${data.myVideoInfo}")
        when (value) {
            DOWNLOAD_STATE_FAIL -> {
                //url, title, MEDIA_TYPE_AUDIO
                getMediaViewModel.downloadMedia(
                    data.download.url,
                    data.download.fileName,
                    if (data.download.fileExtension.equals(
                            "mp3",
                            true
                        )
                    ) MEDIA_TYPE_AUDIO else MEDIA_TYPE_VIDEO
                )
            }
            DOWNLOAD_STATE_INIT,
            DOWNLOAD_STATE_PARSING -> {
                getMediaViewModel.cancelDownload(
                    data.download.url,
                    data.download.fileName,
                    data.download.getMediaType()
                )
                downloadRecordViewModel.delete(data.download)
            }
            DOWNLOAD_STATE_DOWNLOADING -> {
                val id = "${data.download.url}_${data.download.fileExtension}"
                YoutubeDL.getInstance().destroyProcessById(id)
            }
            DOWNLOAD_STATE_DONE -> {
                requireActivity().showPopupMenu(view, R.menu.menu_download_record) { item ->
                    if (item.itemId == R.id.menu_item_delete) {
                        val fileName =
                            "${data.download.fileName}.${data.download.fileExtension}"
                        val file = File(requireActivity().downloadFolder(), fileName)

                        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        val contentResolver = requireActivity().contentResolver
                        val projection = MediaStore.Audio.Media.DATA + "=?"
                        val deleteRows =
                            contentResolver.delete(uri, projection, arrayOf(file.absolutePath))
                        file.deleteOnExit()
                        downloadRecordViewModel.delete(data.download)
                        true
                    } else {
                        false
                    }
                }
            }
            else -> {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val KEY_MODE = "key_mode"
        fun newInstance(mode: Int): DownloadCategoryFragment {
            val f = DownloadCategoryFragment()
            val bundle = Bundle().apply {
                putInt(KEY_MODE, mode)
            }
            f.arguments = bundle
            return f
        }
    }
}