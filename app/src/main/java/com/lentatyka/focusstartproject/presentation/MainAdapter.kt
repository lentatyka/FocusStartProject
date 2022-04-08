package com.lentatyka.focusstartproject.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lentatyka.focusstartproject.databinding.RateItemBinding
import com.lentatyka.focusstartproject.domain.model.Rate

class MainAdapter(private val callback: (String) -> Unit) : ListAdapter<Rate, MainAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private val binding: RateItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Rate){
            binding.root.setOnClickListener {
                //pass item to layer for binding
                callback(item.id)
            }
        }
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<Rate>() {
            override fun areItemsTheSame(oldItem: Rate, newItem: Rate): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Rate, newItem: Rate): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RateItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}