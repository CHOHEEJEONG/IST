package com.example.nav.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nav.BuildConfig
import com.example.nav.MainActivity2
import com.example.nav.databinding.FragmentSettingsBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    val BASE_URL = BuildConfig.BASE_URL
    val url = BASE_URL + "settings"

    lateinit var mainActivity: MainActivity2


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity2

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val swebView: WebView = binding.swebView
        goToWeb(swebView, url)

        swebView.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action != KeyEvent.ACTION_DOWN) return@OnKeyListener true
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (swebView.canGoBack()) {
                    swebView.goBack()
                }
                return@OnKeyListener true
            }
            false
        })
        return root
    }

    override fun onResume() {
        super.onResume()

        // 웹뷰의 자바스크립트 기능을 활성화 시킵니다.
        swebView.settings.javaScriptEnabled = true

        //Android 명의 JavascriptInterface 를 추가해 줍니다.
        swebView.addJavascriptInterface(WebAppInterface(mainActivity), "Android")

    }

    class WebAppInterface(private val mContext: Context) {

        @JavascriptInterface
        fun AppConnection(user: String) {
            Toast.makeText(mContext, "Hello " + user, Toast.LENGTH_SHORT).show()
            val intent = Intent(mContext, MainActivity2::class.java)
            intent.putExtra("id",user)
            ContextCompat.startActivity(mContext, intent, null)

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}