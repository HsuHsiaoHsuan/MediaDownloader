package idv.hsu.media.downloader.utils

import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

fun WebView.initWebView(webViewClient: WebViewClient?, webChromeClient: WebChromeClient?) {
    settings.apply {
        javaScriptEnabled = true
//        setAppCacheEnabled(true)
        domStorageEnabled = true
        setSupportMultipleWindows(true)
        cacheMode = WebSettings.LOAD_DEFAULT
    }

    if (webViewClient != null) {
        setWebViewClient(webViewClient)
    }

    if (webChromeClient != null) {
        setWebChromeClient(webChromeClient)
    }
}