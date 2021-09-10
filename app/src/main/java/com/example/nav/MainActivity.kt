package com.example.nav

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)


        mwebView.settings.javaScriptEnabled = true // 자바 스크립트 허용
        /* 웹뷰에서 새 창이 뜨지 않도록 방지하는 구문 */
        mwebView.webViewClient = WebViewClient()
        mwebView.webChromeClient = WebChromeClient()
        /* 웹뷰에서 새 창이 뜨지 않도록 방지하는 구문 */
        mwebView.loadUrl("http://34.64.143.233:8080/") //링크 주소를 Load (http://34.64.143.233:8080/)

        btn_main.setOnClickListener {
            val intent = Intent(this, MainActivity2:: class.java)
            startActivity(intent);
        }
    }

    override fun onBackPressed() {
        if(mwebView.canGoBack()){
            mwebView.goBack()
        } else {
            super.onBackPressed()
        }
    }



}