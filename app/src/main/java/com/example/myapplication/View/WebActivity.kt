package com.example.myapplication.View

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.webkit.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R


open class WebActivity : AppCompatActivity() {
    var webView: WebView? = null
    var progressDialog: ProgressDialog? = null
    var hasError : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        webView = findViewById<WebView>(R.id.webview)
        showWebView()
        val webSettings: WebSettings = webView!!.getSettings()
        webSettings.javaScriptEnabled = true
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Loading")
        progressDialog!!.show()
    }

    open fun showWebView() {
        val bundle: Bundle? = getIntent().getExtras()
        if (bundle != null) {
            val url = bundle.getString("url")
            if (!TextUtils.isEmpty(url)) {
                webView!!.webViewClient = MyWebViewClient(this)
                webView!!.loadUrl(url!!)
            } else {
                Toast.makeText(
                    this,
                    getResources().getString(R.string.error_url),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    class MyWebViewClient internal constructor(private val activity: WebActivity) : WebViewClient() {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val url: String = request?.url.toString();
            view?.loadUrl(url)
            return true
        }

        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            webView.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            if (activity.hasError) {
                return
            }
            if (activity.progressDialog != null) {
                activity.progressDialog!!.dismiss()
            }
        }

        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            activity.hasError = true
            if (activity.progressDialog != null) {
                activity.progressDialog!!.dismiss()
            }
            Toast.makeText(
                activity,
                activity.getResources().getString(R.string.connection_time_out),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
