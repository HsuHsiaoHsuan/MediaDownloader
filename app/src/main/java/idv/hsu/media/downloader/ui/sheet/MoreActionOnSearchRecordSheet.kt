package idv.hsu.media.downloader.ui.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import idv.hsu.media.downloader.databinding.SheetMoreActionOnSearchRecordBinding
import idv.hsu.media.downloader.ext.reformatFileName
import idv.hsu.media.downloader.worker.MEDIA_TYPE_AUDIO
import idv.hsu.media.downloader.worker.MEDIA_TYPE_VIDEO

@AndroidEntryPoint
class MoreActionOnSearchRecordSheet : BottomSheetDialogFragment() {

    private var _binding: SheetMoreActionOnSearchRecordBinding? = null
    private val binding get() = _binding!!

    private val navArgs: MoreActionOnSearchRecordSheetArgs by navArgs()

    private val viewModel: MoreActionOnSearchRecordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SheetMoreActionOnSearchRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = navArgs.argSearchAndInfo
        val url = data.myVideoInfo?.url ?: ""
        val title =
            data.myVideoInfo?.title?.reformatFileName() ?: System.currentTimeMillis().toString()

        binding.textDownloadVideo.setOnClickListener {
            viewModel.downloadMedia(url, title, MEDIA_TYPE_VIDEO)
            findNavController().popBackStack()
        }
        binding.textDownloadAudio.setOnClickListener {
            viewModel.downloadMedia(url, title, MEDIA_TYPE_AUDIO)
            findNavController().popBackStack()
        }
        binding.textDeleteRecord.setOnClickListener {
            viewModel.delSearchRecord(data)
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}