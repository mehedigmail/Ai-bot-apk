
package com.example.aitradingsignal

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var statusText: TextView
    private lateinit var signalText: TextView

    private val backgroundColor = Color.rgb(8, 13, 24)
    private val panelColor = Color.rgb(20, 29, 44)
    private val greenColor = Color.rgb(0, 200, 150)

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(backgroundColor)
        }

        val header = TextView(this).apply {
            text = "AI TRADING SIGNAL"
            textSize = 20f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(12, 18, 12, 18)
            setBackgroundColor(panelColor)
        }

        statusText = TextView(this).apply {
            text = "Status: Waiting for Quotex"
            textSize = 12f
            setTextColor(greenColor)
            setPadding(12, 10, 12, 10)
        }

        webView = WebView(this).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.loadsImagesAutomatically = true
            settings.javaScriptCanOpenWindowsAutomatically = false
            settings.setSupportMultipleWindows(false)

            webViewClient = object : WebViewClient() {
                override fun onPageStarted(
                    view: WebView?,
                    url: String?,
                    favicon: android.graphics.Bitmap?
                ) {
                    statusText.text = "Status: Loading Quotex..."
                }

                override fun onPageFinished(
                    view: WebView?,
                    url: String?
                ) {
                    statusText.text = "Status: Quotex page loaded"
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: android.webkit.WebResourceRequest?,
                    error: android.webkit.WebResourceError?
                ) {
                    if (request?.isForMainFrame == true) {
                        statusText.text = "Status: Page loading failed"
                    }
                }
            }

            webChromeClient = WebChromeClient()
            setBackgroundColor(Color.BLACK)
            loadUrl("https://qxbroker.com/")
        }

        val signalPanel = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 12, 16, 16)
            setBackgroundColor(panelColor)
        }

        val panelTitle = TextView(this).apply {
            text = "NEXT CANDLE SIGNAL"
            textSize = 15f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }

        signalText = TextView(this).apply {
            text = "WAIT"
            textSize = 30f
            setTextColor(Color.YELLOW)
            gravity = Gravity.CENTER
            setPadding(8, 12, 8, 12)
        }

        val infoText = TextView(this).apply {
            text = "AI analysis is not started yet"
            textSize = 12f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
        }

        val buttonRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }

        val startButton = Button(this).apply {
            text = "Start Analysis"
            setOnClickListener {
                signalText.text = "WAIT"
                signalText.setTextColor(Color.YELLOW)
                infoText.text =
                    "Screen analysis will be added in the next steps."
                Toast.makeText(
                    this@MainActivity,
                    "Analysis system is not connected yet",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val stopButton = Button(this).apply {
            text = "Stop"
            setOnClickListener {
                signalText.text = "WAIT"
                signalText.setTextColor(Color.YELLOW)
                infoText.text = "Analysis stopped"
            }
        }

        buttonRow.addView(
            startButton,
            LinearLayout.LayoutParams(0, 52, 1f)
        )

        buttonRow.addView(
            stopButton,
            LinearLayout.LayoutParams(0, 52, 1f)
        )

        signalPanel.addView(panelTitle)
        signalPanel.addView(signalText)
        signalPanel.addView(infoText)
        signalPanel.addView(buttonRow)

        root.addView(header)
        root.addView(statusText)

        root.addView(
            webView,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        root.addView(signalPanel)

        setContentView(root)
    }

    override fun onBackPressed() {
        if (::webView.isInitialized && webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        if (::webView.isInitialized) {
            webView.destroy()
        }
        super.onDestroy()
    }
}

