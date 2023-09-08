package com.tierradeatomos.gasolineras

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

class MyWebViewClient internal constructor(private val activity: Activity) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url: String = request?.url.toString()

        if (url.contains("http://gasolineras.", false))
            return false

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        activity.startActivity(intent)
        return true
    }

    override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
        if (url.contains("http://gasolineras."))
            return false

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        activity.startActivity(intent)
        return true
    }

    override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
        Toast.makeText(activity, "Got Error! $error", Toast.LENGTH_SHORT).show()
    }

}
