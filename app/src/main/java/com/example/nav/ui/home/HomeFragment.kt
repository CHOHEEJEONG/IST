package com.example.nav.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.nav.R
import com.example.nav.databinding.FragmentHomeBinding
import android.widget.RadioButton



class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val selectedOSeason: TextView = binding.selectedOSeason
        val selectedDSeason: TextView = binding.selectedDSeason
        val originalGroup: RadioGroup = binding.originalSeason
        val desiredGroup: RadioGroup = binding.desiredSeason
        val btn_transfer: Button = binding.btnTransfer

        originalGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.o_spring -> selectedOSeason.setText("spring")
                R.id.o_summer -> selectedOSeason.setText("summer")
                R.id.o_autumn -> selectedOSeason.setText("autumn")
                R.id.o_winter -> selectedOSeason.setText("winter")
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
