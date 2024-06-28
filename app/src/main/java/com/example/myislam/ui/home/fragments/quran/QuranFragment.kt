package com.example.myislam.ui.home.fragments.quran

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myislam.data.data_providers.quran_sura.QuranSuraDataProvider
import com.example.myislam.data.models.QuranSura
import com.example.myislam.databinding.FragmentQuranBinding
import com.example.myislam.ui.sura.SuraActivity
import com.example.myislam.utils.Constants

class QuranFragment : Fragment() {
    private var _binding: FragmentQuranBinding? = null
    private val binding: FragmentQuranBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuranBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeSuraRecyclerView()
    }

    private fun initializeSuraRecyclerView() {
        val adapter = SuraRecyclerViewAdapter(QuranSuraDataProvider.getSuraList())
        adapter.setOnSuraClickListener(::startSuraActivity)
        binding.recyclerView.adapter = adapter
    }

    private fun startSuraActivity(position: Int, sura: QuranSura) {
        val intent = Intent(requireContext(), SuraActivity::class.java).apply {
            putExtra(Constants.SURA_POSITION, position)
            putExtra(Constants.SURA, sura)
        }
        startActivity(intent)
    }
}
