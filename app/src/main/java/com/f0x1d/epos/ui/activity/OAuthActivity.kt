package com.f0x1d.epos.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.f0x1d.epos.R
import com.f0x1d.epos.ui.activity.base.BaseActivity
import com.f0x1d.epos.viewmodel.OAuthViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator

class OAuthActivity : BaseActivity<OAuthViewModel>() {

    private lateinit var webView: WebView
    private lateinit var loadingProgress: LinearProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oauth)

        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)

                if (url.startsWith("diaryperm://redirect?")) {
                    viewModel.grabToken(url)
                }
            }
        }

        loadingProgress = findViewById(R.id.loading_progress)

        viewModel.authStateData.observe(this) {
            when (it) {
                OAuthViewModel.AuthState.LOGGING -> {
                    webView.visibility = View.VISIBLE
                    loadingProgress.visibility = View.GONE

                    webView.loadUrl(viewModel.oauthLink())
                }
                OAuthViewModel.AuthState.GRABBING -> {
                    webView.visibility = View.GONE
                    loadingProgress.visibility = View.VISIBLE
                }
                OAuthViewModel.AuthState.AUTHORIZED -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack())
            webView.goBack()
        else
            super.onBackPressed()
    }

    override fun viewModel(): Class<OAuthViewModel> = OAuthViewModel::class.java
}