package com.example.nav

import android.content.Intent
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val naver = "http://naver.com"
        val url = "http://34.64.143.233:8080/login"
        goToWeb(mwebView,naver)

        btn_main.setOnClickListener {
            val intent = Intent(this, MainActivity2:: class.java)
            startActivity(intent);
        }
    }

    fun goToWeb(view: WebView, url: String) {
        view.settings.javaScriptEnabled = true // 자바 스크립트 허용
        /* 웹뷰에서 새 창이 뜨지 않도록 방지하는 구문 */
        view.webViewClient = WebViewClient()
        view.webChromeClient = WebChromeClient()
        /* 웹뷰에서 새 창이 뜨지 않도록 방지하는 구문 */
        view.loadUrl(url)
    }

    override fun onBackPressed() {
        if(mwebView.canGoBack()){
            mwebView.goBack()
        } else {
            super.onBackPressed()
        }
    }



}