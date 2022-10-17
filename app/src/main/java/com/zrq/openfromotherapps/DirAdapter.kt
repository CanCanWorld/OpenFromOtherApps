package com.zrq.openfromotherapps

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zrq.openfromotherapps.databinding.ItemDirBinding

class DirAdapter(
    private val context: Context,
    private val list: ArrayList<String>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<VH<ItemDirBinding>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<ItemDirBinding> {
        return VH(ItemDirBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: VH<ItemDirBinding>, position: Int) {
        holder.binding.tvDir.text = list[position]
        holder.binding.ivNext.setOnClickListener {
            onItemClickListener.onNextClick(it, position)
        }
        holder.binding.root.setOnClickListener {
            onItemClickListener.onItemClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}