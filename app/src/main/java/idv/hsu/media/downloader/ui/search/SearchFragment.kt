package idv.hsu.media.downloader.ui.search

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import idv.hsu.media.downloader.MobileNavigationDirections
import idv.hsu.media.downloader.databinding.FragmentSearchBinding
import idv.hsu.media.downloader.db.relation.SearchAndInfo
import idv.hsu.media.downloader.utils.openBrowser
import idv.hsu.media.downloader.viewmodel.ParseMediaViewModel
import idv.hsu.media.downloader.viewmodel.SearchRecordUiState
import idv.hsu.media.downloader.viewmodel.SearchRecordViewModel
import idv.hsu.media.downloader.vo.SEARCH_TYPE_TEXT_INPUT
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.roundToInt

@AndroidEntryPoint
class SearchFragment : Fragment(), SearchAdapter.OnSearchRecordClickListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchRecordViewModel: SearchRecordViewModel by viewModels()
    private val parseMediaViewModel: ParseMediaViewModel by viewModels()

    private val adapter = SearchAdapter().apply {
        listener = this@SearchFragment
    }

//    private val Int.dp
//        get() = TypedValue.applyDimension(
//            TypedValue.COMPLEX_UNIT_DIP,
//            toFloat(), resources.displayMetrics
//        ).roundToInt()

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

        // TODO swipe to delete
        // https://medium.com/getpowerplay/understanding-swipe-and-drag-gestures-in-recyclerview-cb3136beff20
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
                searchRecordViewModel.allSearchByInput.collect {
                    adapter.setData(it)
                }
            }
        }
    }

    override fun onItemClick(data: SearchAndInfo) {
        Timber.d("onItemClick, data: $data")
        requireActivity().openBrowser(data.search.url)
    }

    override fun onActionDownloadClick(data: SearchAndInfo, view: View) {
        Timber.d("onActionDownloadClick, data: $data")
    }

    override fun onActionDeleteClick(data: SearchAndInfo) {
        Timber.d("onActionDeleteClick, data: $data")
        searchRecordViewModel.deleteSearch(data.search)
    }

    override fun onActionRefreshClick(data: SearchAndInfo) {
        Timber.d("onActionRefreshClick, data: $data")
        parseMediaViewModel.getVideoInfo(data.search.url)
    }

    override fun onActionMoreClick(data: SearchAndInfo, view: View) {
        Timber.d("onActionMoreClick, data: $data")
        findNavController().navigate(
            MobileNavigationDirections.actionGlobalOpenSheetMoreAction(data)
        )
    }

    private fun search() {
        binding.inputUrl.text?.let {
            val url = it.trim().toString()
            if (url.isNotBlank()) {
                searchRecordViewModel.addSearch(url, SEARCH_TYPE_TEXT_INPUT)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.listSearch.adapter = null
        _binding = null
    }
}