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
import dagger.hilt.android.AndroidEntryPoint
import idv.hsu.media.downloader.R
import idv.hsu.media.downloader.databinding.FragmentSearchBinding
import idv.hsu.media.downloader.db.relation.SearchAndInfo
import idv.hsu.media.downloader.ext.reformatFileName
import idv.hsu.media.downloader.utils.openBrowser
import idv.hsu.media.downloader.utils.showPopupMenu
import idv.hsu.media.downloader.viewmodel.GetMediaViewModel
import idv.hsu.media.downloader.viewmodel.ParseMediaViewModel
import idv.hsu.media.downloader.worker.MEDIA_TYPE_AUDIO
import idv.hsu.media.downloader.worker.MEDIA_TYPE_VIDEO
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.roundToInt

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

    private val Int.dp
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            toFloat(), resources.displayMetrics
        ).roundToInt()

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

        // https://medium.com/getpowerplay/understanding-swipe-and-drag-gestures-in-recyclerview-cb3136beff20
        /*
        val displayMetrics = resources.displayMetrics
        val height = (displayMetrics.heightPixels / displayMetrics.density).toInt().dp
        val width = (displayMetrics.widthPixels / displayMetrics.density).toInt().dp
        val delIcon =
            resources.getDrawable(R.drawable.ic_delete_24, requireActivity().theme)
                .apply {
                    setTint(Color.WHITE)
                }
        val delColor = resources.getColor(android.R.color.holo_red_light, null)

        val itemTouchHelper =
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val pos = viewHolder.adapterPosition
//                searchRecordViewModel.deleteSearch()
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )

//                    c.drawColor(Color.RED)
                    val textMargin =
                        resources.getDimension(R.dimen.activity_vertical_margin).roundToInt()
                    delIcon.bounds = Rect(
                        width - textMargin - delIcon.intrinsicWidth,
                        viewHolder.itemView.top + textMargin + 1.dp,
                        width - textMargin,
                        viewHolder.itemView.top + delIcon.intrinsicHeight + textMargin + 1.dp
                    )
                    if (dX > 0) delIcon.draw(c)


                }
            })
        itemTouchHelper.attachToRecyclerView(binding.listSearch)
         */

        //val swipeController = SwipeController(object: SwipeControllerActions {
        //            override fun onRightClicked(position: Int) {
        //                super.onRightClicked(position)
        //            }
        //        })
        //        val itemTouchHelper = ItemTouchHelper(swipeController)
        //        itemTouchHelper.attachToRecyclerView(binding.listSearch)
        //        binding.listSearch.addItemDecoration(object : RecyclerView.ItemDecoration() {
        //            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        //                swipeController.onDraw(c)
        //            }
        //        })

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
        requireActivity().openBrowser(data.search.url)
    }

    override fun onDownloadClick(data: SearchAndInfo, view: View) {
        val url = data.myVideoInfo?.url
        val title =
            data.myVideoInfo?.title?.reformatFileName() ?: System.currentTimeMillis().toString()
        requireActivity().showPopupMenu(view, R.menu.menu_search_record) { item ->
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