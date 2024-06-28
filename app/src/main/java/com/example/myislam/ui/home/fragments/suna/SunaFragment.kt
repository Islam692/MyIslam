package com.example.myislam.ui.home.fragments.suna

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myislam.databinding.FragmentSunaBinding

class SunaFragment : Fragment() {
    private var _binding: FragmentSunaBinding? = null
    private val binding: FragmentSunaBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSunaBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}