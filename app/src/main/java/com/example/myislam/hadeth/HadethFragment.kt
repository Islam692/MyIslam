package com.example.myislam.hadeth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myislam.Constants
import com.example.myislam.databinding.FragmentHadethBinding
import com.example.myislam.hadethDetails.HadethDetailsActivity
import com.example.myislam.model.Hadeth

class HadethFragment : Fragment() {
    lateinit var viewBinding: FragmentHadethBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentHadethBinding.inflate(layoutInflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    lateinit var adapter: HadethRecyclerAdapter
    private fun initViews() {
        initRecyclerView()
        loadhadethfile()
        bindHadethList()
    }

    private fun initRecyclerView() {
        adapter = HadethRecyclerAdapter(null)
        adapter.onItemClickListener = HadethRecyclerAdapter.OnItemClickListener { pos, hadeth ->
            showHadethDetails(hadeth)
        }
        viewBinding.recyclerView.adapter = adapter
    }

    private fun showHadethDetails(hadeth: Hadeth) {
        val intent = Intent(context, HadethDetailsActivity::class.java)
        intent.putExtra(Constants.EXTRA_HADETH, hadeth)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()

    }

    private fun bindHadethList() {
        adapter.bindItems(hadethList)
    }

    val hadethList = mutableListOf<Hadeth>()
    private fun loadhadethfile() {
        val fileContent =
            requireActivity().assets.open("ahadeth.txt").bufferedReader().use { it.readText() }
        val singleHadethList = fileContent.trim().split("#")
        singleHadethList.forEach { element ->
            val lines = element.trim().split("\n")
            val title = lines[0]
            val content = lines.joinToString("\n")
            val hadeth = Hadeth(title, content)
            hadethList.add(hadeth)
        }
    }
}