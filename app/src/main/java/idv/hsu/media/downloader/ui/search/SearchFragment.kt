package idv.hsu.media.downloader.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import idv.hsu.media.downloader.R
import idv.hsu.media.downloader.databinding.FragmentSearchBinding
import idv.hsu.media.downloader.db.relation.SearchAndInfo
import idv.hsu.media.downloader.ext.reformatFileName
import idv.hsu.media.downloader.viewmodel.GetMediaViewModel
import idv.hsu.media.downloader.viewmodel.ParseMediaViewModel
import idv.hsu.media.downloader.worker.MEDIA_TYPE_AUDIO
import idv.hsu.media.downloader.worker.MEDIA_TYPE_VIDEO
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : Fragment(), SearchAdapter.OnSearchRecordClickListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchRecordViewModel: SearchRecordViewModel by viewModels()
    private val parseMediaViewModel: ParseMediaViewModel by viewModels()
    private val getMediaViewModel: GetMediaViewModel by viewModels()

    private val adapter = SearchAdapter().apply {
        listener = this@SearchFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.inputUrl.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search()
                true
            } else {
                false
            }
        }

        binding.buttonSearch.setOnClickListener {
            search()
        }

        with(binding.listSearch) {
            adapter = this@SearchFragment.adapter
            setHasFixedSize(true)
        }

        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchRecordViewModel.uiState.collect {
                    when (it) {
                        is SearchRecordUiState.AddSearchOk -> { // a new one
                            parseMediaViewModel.getVideoInfo(it.url)
                        }
                        is SearchRecordUiState.AddSearchNone -> { // already have
                            Timber.e("FREEMAN, add search none: ${it.url}")
                        }
                        is SearchRecordUiState.AddSearchFail -> { // add fail
                            Timber.e("FREEMAN, add search fail: ${it.url}")
                        }
                        else -> {}
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchRecordViewModel.allSearchAndInfo.collect {
//                    Timber.e("FREEMAN, all searchNinfo: $it")
                    adapter.setData(it)
                }
            }
        }
    }

    override fun onItemClick(data: SearchAndInfo) {
        Timber.e("FREEMAN, onItemClick: $data")
    }

    override fun onDownloadClick(data: SearchAndInfo, view: View) {
        val url = data.myVideoInfo?.url
        val title =
            data.myVideoInfo?.title?.reformatFileName() ?: System.currentTimeMillis().toString()
        PopupMenu(requireActivity(), view).apply {
            menuInflater.inflate(R.menu.menu_search_record, this.menu)
            if (this.menu is MenuBuilder) {
                val menuBuilder = this.menu as MenuBuilder
                menuBuilder.setOptionalIconsVisible(true)
            }
            setOnMenuItemClickListener { item: MenuItem ->
                when (item.itemId) {
                    R.id.menu_item_audio -> {
                        if (url != null) {
                            getMediaViewModel.downloadMedia(url, title, MEDIA_TYPE_AUDIO)
                        }
                        true
                    }
                    R.id.menu_item_video -> {
                        if (url != null) {
                            getMediaViewModel.downloadMedia(url, title, MEDIA_TYPE_VIDEO)
                        }
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }.show()
    }

    private fun search() {
        binding.inputUrl.text?.let {
            val url = it.trim().toString()
            if (url.isNotBlank()) {
                searchRecordViewModel.addSearch(url)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.listSearch.adapter = null
        _binding = null
    }
}