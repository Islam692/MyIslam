package com.example.myislam.ui.home.fragments.hadeth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myislam.data.models.Hadeth
import com.example.myislam.databinding.ItemHadethBinding

class HadethRecyclerAdapter(
    private var hadethList: List<Hadeth>
) :
    RecyclerView.Adapter<HadethRecyclerAdapter.ViewHolder>() {

    private var onHadethClickListener: OnHadethClickListener? = null

    fun setOnHadethClickListener(onHadethClickListener: OnHadethClickListener) {
        this.onHadethClickListener = onHadethClickListener
    }

    fun interface OnHadethClickListener {
        fun onHadethClick(hadeth: Hadeth)
    }

    class ViewHolder(val binding: ItemHadethBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding =
            ItemHadethBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBinding)
    }

    override fun getItemCount(): Int = hadethList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hadeth = hadethList[position]
        holder.binding.title.text = hadeth.title
        holder.binding.root.setOnClickListener { onHadethClickListener?.onHadethClick(hadeth) }
    }
}