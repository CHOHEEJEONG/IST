package com.example.nav

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val naver = "http://naver.com"
        val url = "http://34.64.143.233:8080/"
        val url2 = "http://34.64.81.103:8080/"
        goToWeb(mwebView,url2)

        // main2로 가기 위한 임시 버튼 생성 : 최종 작업이 끝나면 url 변경 후 지울 예정
        btn_main.setOnClickListener {
            val intent = Intent(this, MainActivity2:: class.java)
            startActivity(intent)
        }


    }

    override fun onResume() {
        super.onResume()
        val myWebView: WebView = mwebView

        // 웹뷰의 자바스크립트 기능을 활성화 시킵니다.
        myWebView.settings.javaScriptEnabled = true

        //Android 명의 JavascriptInterface 를 추가해 줍니다.
        myWebView.addJavascriptInterface(WebAppInterface(this), "Android")

        //웹을 로딩합니다.
        val url = "http://34.64.81.103:8080/"
        myWebView.loadUrl(url)
    }

    /** Instantiate the interface and set the context  */
    class WebAppInterface(private val mContext: Context) {

        /** Show a toast from the web page  */
        @JavascriptInterface
        fun showToast(toast: String) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
            startActivity(mContext,Intent(mContext,MainActivity2::class.java),null)

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