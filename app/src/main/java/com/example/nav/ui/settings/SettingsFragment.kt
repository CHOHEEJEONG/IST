package com.example.nav.ui.settings

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nav.BuildConfig
import com.example.nav.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    val BASE_URL = BuildConfig.BASE_URL
    val url = BASE_URL + "settings"

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