package com.example.nav

import android.Manifest
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
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val BASE_URL = BuildConfig.BASE_URL


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        goToWeb(mwebView,BASE_URL)

        // main2로 가기 위한 임시 버튼 생성 : 최종 작업이 끝나면 url 변경 후 지울 예정
        btn_main.setOnClickListener {
            val intent = Intent(this, MainActivity2:: class.java)
            startActivity(intent)
        }

        setPermission()

    }

    override fun onResume() {
        super.onResume()
        val myWebView: WebView = mwebView

        // 웹뷰의 자바스크립트 기능을 활성화 시킵니다.
        myWebView.settings.javaScriptEnabled = true

        //Android 명의 JavascriptInterface 를 추가해 줍니다.
        myWebView.addJavascriptInterface(WebAppInterface(this), "Android")

        //웹을 로딩합니다.
        myWebView.loadUrl(BASE_URL)
    }

    class WebAppInterface(private val mContext: Context) {

        @JavascriptInterface
        fun AppConnection(user: String) {
            Toast.makeText(mContext, "Hello " + user, Toast.LENGTH_SHORT).show()
            val intent = Intent(mContext, MainActivity2::class.java)
            intent.putExtra("id",user)
            startActivity(mContext, intent, null)

        }

    }

    // 테드 퍼미션 설정 (카메라 사용시 권한 설정 팝업을 쉽게 구현하기 위해 사용)
    fun setPermission() {
        val permission = object : PermissionListener {
            override fun onPermissionGranted() {//설정해 놓은 위험권한(카메라 접근 등)이 허용된 경우 이곳을 실행
                Toast.makeText(this@MainActivity, "요청하신 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {//설정해 놓은 위험권한이 거부된 경우 이곳을 실행
                Toast.makeText(this@MainActivity, "요청하신 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permission)
            .setRationaleMessage("카메라 앱을 사용하시려면 권한을 허용해주세요.")
            .setDeniedMessage("권한을 거부하셨습니다.앱을 사용하시려면 [앱 설정]-[권한] 항목에서 권한을 허용해주세요.")
            .setPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
            )
            .check()
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