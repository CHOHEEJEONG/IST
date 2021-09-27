package com.example.nav

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient


class TransferFragment : Fragment() {

    val BASE_URL = BuildConfig.BASE_URL
    val url = BASE_URL + "transform"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view: View = inflater.inflate(R.layout.fragment_transfer, container, false)


        (activity as MainActivity2).supportActionBar?.title = getString(R.string.title_transfer)

        val twebView: WebView = view.findViewById<WebView>(R.id.twebView)

        twebView!!.settings.javaScriptEnabled = true
        twebView.webViewClient = WebViewClient()
        twebView.loadUrl(url)
        twebView.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (twebView != null) {
                        if (twebView.canGoBack()) {
                            twebView.goBack()
                        } else {
                            requireActivity().onBackPressed()
                        }
                    }
                }
            }
            true
        }
        return view
    }




}