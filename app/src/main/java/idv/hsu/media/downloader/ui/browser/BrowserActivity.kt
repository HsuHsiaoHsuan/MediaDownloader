package idv.hsu.media.downloader.ui.browser

import android.os.Bundle
import android.util.TypedValue
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import idv.hsu.media.downloader.databinding.ActivityBrowserBinding
import idv.hsu.media.downloader.utils.EXTRA_URL
import idv.hsu.media.downloader.utils.initWebView

@AndroidEntryPoint
class BrowserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBrowserBinding

    private val viewModel: BrowserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBrowserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleGoBack()
            }
        })

        binding.webview.initWebView(
            object : WebViewClient() {
                override fun doUpdateVisitedHistory(
                    view: WebView?,
                    url: String?,
                    isReload: Boolean
                ) {
                    super.doUpdateVisitedHistory(view, url, isReload)
                    binding.includeUrl.editWebUrl.setText(url ?: "")
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    binding.swipe.isRefreshing = false
                }
            },
            object : WebChromeClient() {
            }
        )

        with(binding.swipe) {
            val typedValue = TypedValue()
            theme.resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true)
            val color = typedValue.data
            setColorSchemeColors(color)
            setOnRefreshListener {
                binding.webview.reload()
            }
        }

        binding.buttonAction.setOnClickListener {
            handleGoBack()
        }


        binding.fabDownload.setOnClickListener { view ->
            val url = binding.webview.url
            if (url != null) {
                viewModel.getMediaInfo(url)
                Toast.makeText(this@BrowserActivity, "Added to list!", Toast.LENGTH_LONG).show()
            }
        }

        intent.getStringExtra(EXTRA_URL)?.also {
            binding.webview.loadUrl(it)
        }

        binding.buttonClose.setOnClickListener {
            finish()
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