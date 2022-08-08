package idv.hsu.media.downloader.ui.home

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import dagger.hilt.android.AndroidEntryPoint
import idv.hsu.media.downloader.MobileNavigationDirections
import idv.hsu.media.downloader.R
import idv.hsu.media.downloader.databinding.FragmentHomeBinding
import idv.hsu.media.downloader.db.relation.SearchAndInfo
import idv.hsu.media.downloader.ui.search.RecommendAdapter
import idv.hsu.media.downloader.ui.search.SearchAdapter
import idv.hsu.media.downloader.utils.openBrowser
import idv.hsu.media.downloader.viewmodel.GetMediaViewModel
import idv.hsu.media.downloader.viewmodel.SearchRecordViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment(), RecommendAdapter.OnRecommendClickListener,
    SearchAdapter.OnSearchRecordClickListener {

    private val recommend: List<Triple<String, String, Int>> = listOf(
        Triple("Google Video", "https://www.google.com/videohp", R.mipmap.ic_launcher_round),
        Triple("YouTube", "https://m.youtube.com/", R.mipmap.ic_launcher_round),
        Triple("Facebook Watch", "https://m.facebook.com/watch/", R.mipmap.ic_launcher_round),
        Triple("Vimeo Watch", "https://vimeo.com/watch/", R.mipmap.ic_launcher_round),
        Triple("SoundCloud Discover", "https://m.soundcloud.com/discover/", R.mipmap.ic_launcher_round),
        Triple("DailyMotion", "https://www.dailymotion.com/", R.mipmap.ic_launcher_round),
        Triple("Pornhub", "https://www.pornhub.com/", R.mipmap.ic_launcher_round)
    )

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val getMediaViewModel: GetMediaViewModel by viewModels()
    private val searchRecordViewMode: SearchRecordViewModel by viewModels()

    private val recommendAdapter = RecommendAdapter(recommend, this@HomeFragment)
    private val searchAdapter = SearchAdapter().apply {
        listener = this@HomeFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.listRecommend) {
            adapter = this@HomeFragment.recommendAdapter
            setHasFixedSize(true)
            GravitySnapHelper(Gravity.START).attachToRecyclerView(this)
        }
        with(binding.listDownloadQueue) {
            adapter = this@HomeFragment.searchAdapter
            setHasFixedSize(true)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchRecordViewMode.allSearchByWebview.collectLatest {
                    searchAdapter.setData(it)
                }
            }
        }
    }

    override fun onRecommendItemClick(url: String) {
        requireActivity().openBrowser(url)
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
        searchRecordViewMode.deleteSearch(data.search)
    }

    override fun onActionRefreshClick(data: SearchAndInfo) {
        Timber.d("onActionRefreshClick, data: $data")
    }

    override fun onActionMoreClick(data: SearchAndInfo, view: View) {
        Timber.d("onActionMoreClick, data: $data")
        findNavController().navigate(
            MobileNavigationDirections.actionGlobalOpenSheetMoreAction(data)
        )
    }
}