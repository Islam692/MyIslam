package com.example.myislam.ui.home.fragments.hadeth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myislam.data.models.Hadeth
import com.example.myislam.databinding.ItemHadethBinding

class HadethRecyclerAdapter(
    private var items: List<Hadeth>?
) :
    RecyclerView.Adapter<HadethRecyclerAdapter.viewHolder>() {

    var onItemClickListener: OnItemClickListener? = null

    fun interface OnItemClickListener {
        fun onItemClick(position: Int, item: Hadeth)
    }

    class viewHolder(val viewBinding: ItemHadethBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val viewBinding =
            ItemHadethBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(viewBinding)
    }

    override fun getItemCount(): Int = items?.size ?: 0

    fun bindItems(newList: List<Hadeth>) {
        items = newList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.viewBinding.title.text = items!![position].title
        if (onItemClickListener != null) {
            holder.viewBinding.root.setOnClickListener {
                onItemClickListener?.onItemClick(position, items!![position])
            }
        }
    }
}