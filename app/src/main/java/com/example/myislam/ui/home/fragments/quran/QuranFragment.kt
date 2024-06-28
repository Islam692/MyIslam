package com.example.myislam.ui.home.fragments.quran

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myislam.Constants
import com.example.myislam.chapterDetails.ChapterDetailsActivity
import com.example.myislam.data.models.QuranSura
import com.example.myislam.databinding.FragmentQuranBinding

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
        val intent = Intent(requireContext(), ChapterDetailsActivity::class.java).apply {
            putExtra(Constants.SURA_POSITION, position)
            putExtra(Constants.SURA, sura)
        }
        startActivity(intent)
    }
}
