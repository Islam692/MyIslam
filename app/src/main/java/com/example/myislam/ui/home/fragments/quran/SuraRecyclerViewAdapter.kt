package com.example.myislam.ui.home.fragments.quran

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myislam.data.models.QuranSura
import com.example.myislam.databinding.ItemSuraBinding

class SuraRecyclerViewAdapter(
    private val suraList: List<QuranSura>
) :
    RecyclerView.Adapter<SuraRecyclerViewAdapter.ViewHolder>() {

    private var onSuraClickListener: OnSuraClickListener? = null

    fun setOnSuraClickListener(listener: OnSuraClickListener) {
        onSuraClickListener = listener
    }

    fun interface OnSuraClickListener {
        fun onSuraClick(position: Int, sura: QuranSura)
    }

    class ViewHolder(val binding: ItemSuraBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSuraBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = suraList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sura = suraList[position]
        holder.binding.sura = sura
        holder.binding.root.setOnClickListener {
            onSuraClickListener?.onSuraClick(position, sura)
        }
    }
}