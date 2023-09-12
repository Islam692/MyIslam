package com.example.myislam.quran

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myislam.databinding.ItemChapterNameBinding

class ChapterNameRecyclerAdapter(
    private val names: List<String>,
    private val counter: List<String>,
    private val numbers: List<String>
) :
    RecyclerView.Adapter<ChapterNameRecyclerAdapter.viewHolder>() {

    var onItemClickListener: OnItemClickListener? = null

    fun interface OnItemClickListener {
        fun onItemClick(position: Int, name: String)
    }

    class viewHolder(val viewBinding: ItemChapterNameBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val viewBinding =
            ItemChapterNameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(viewBinding)
    }

    override fun getItemCount(): Int = names.size

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.viewBinding.title.text = names[position]
        holder.viewBinding.counter.text = counter[position]
        holder.viewBinding.number.text = numbers[position]
        if (onItemClickListener != null) {
            holder.viewBinding.root.setOnClickListener {
                onItemClickListener?.onItemClick(position, names[position])
            }
        }
    }
}