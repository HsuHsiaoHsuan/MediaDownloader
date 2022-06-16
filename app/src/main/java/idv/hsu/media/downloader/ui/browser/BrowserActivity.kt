package idv.hsu.media.downloader.ui.browser

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import idv.hsu.media.downloader.R
import idv.hsu.media.downloader.databinding.ActivityBrowserBinding
import idv.hsu.media.downloader.utils.EXTRA_URL
import idv.hsu.media.downloader.utils.showPopupMenu
import idv.hsu.media.downloader.viewmodel.GetMediaViewModel
import idv.hsu.media.downloader.worker.MEDIA_TYPE_AUDIO
import idv.hsu.media.downloader.worker.MEDIA_TYPE_VIDEO
import timber.log.Timber

@AndroidEntryPoint
class BrowserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBrowserBinding

    private val getMediaViewModel: GetMediaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBrowserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleGoBack()
            }
        })

        with(binding.webview) {
            this.settings.apply {
                javaScriptEnabled = true
                setAppCacheEnabled(true)
                domStorageEnabled = true
                setSupportMultipleWindows(true)
                cacheMode = WebSettings.LOAD_DEFAULT
            }
            webViewClient = object : WebViewClient() {

                override fun doUpdateVisitedHistory(
                    view: WebView?,
                    url: String?,
                    isReload: Boolean
                ) {
                    super.doUpdateVisitedHistory(view, url, isReload)
                    binding.editWebUrl.setText(url ?: "")
                }
            }

            webChromeClient = object : WebChromeClient() {
            }
        }

        binding.buttonAction.setOnClickListener {
            handleGoBack()
        }


        binding.fabDownload.setOnClickListener { view ->
            showPopupMenu(view, R.menu.menu_search_record) { item ->
                val url = binding.webview.url
                val title = binding.webview.title ?: "abc ${Math.random()}"
                Timber.e("FREEMAN, title: $title")
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

        intent.getStringExtra(EXTRA_URL)?.also {
            binding.webview.loadUrl(it)
        }
    }

    private fun handleGoBack() {
        if (binding.webview.canGoBack()) {
            binding.webview.goBack()
        } else {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.webview.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.webview.onPause()
    }
}