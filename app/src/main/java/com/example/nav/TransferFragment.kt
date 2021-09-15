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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view: View = inflater.inflate(R.layout.fragment_transfer, container, false)

        val twebView: WebView = view.findViewById<WebView>(R.id.twebView)
        val url = "http://34.64.143.233:8080/transfer"
        val daum = "http://www.daum.net/"

        twebView!!.settings.javaScriptEnabled = true
        twebView.webViewClient = WebViewClient()
        twebView.loadUrl(daum)
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