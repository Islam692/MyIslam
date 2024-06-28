package com.example.myislam.ui.home.fragments.hadeth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myislam.data.data_providers.hadeth.HadethDataProvider
import com.example.myislam.data.models.Hadeth
import com.example.myislam.databinding.FragmentHadethBinding
import com.example.myislam.ui.hadeth_details.HadethDetailsActivity
import com.example.myislam.utils.Constants

class HadethFragment : Fragment() {
    private var _binding: FragmentHadethBinding? = null
    private val binding: FragmentHadethBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHadethBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val hadethList = HadethDataProvider.getHadethList(requireActivity())
        val hadethAdapter = HadethRecyclerAdapter(hadethList)
        hadethAdapter.setOnHadethClickListener(::showHadethDetails)
        binding.recyclerView.adapter = hadethAdapter
    }

    private fun showHadethDetails(hadeth: Hadeth) {
        val intent = Intent(requireActivity(), HadethDetailsActivity::class.java)
        intent.putExtra(Constants.HADETH, hadeth)
        startActivity(intent)
    }
}
