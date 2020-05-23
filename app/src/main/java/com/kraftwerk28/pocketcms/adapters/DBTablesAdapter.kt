package com.kraftwerk28.pocketcms.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kraftwerk28.pocketcms.databinding.ItemTableBinding
import com.kraftwerk28.pocketcms.viewmodels.DBTablesViewModel

class TableItemViewHolder(val itemBinding: ItemTableBinding) :
    RecyclerView.ViewHolder(itemBinding.root)

class TableItemDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem
}

class DBTablesAdapter(
    val viewModel: DBTablesViewModel,
    val onTableItemClick: (tableName: String) -> Unit
) : ListAdapter<String, TableItemViewHolder>(TableItemDiffCallback()) {

    override fun onBindViewHolder(holder: TableItemViewHolder, position: Int) {
        holder.itemBinding.cardView.setOnClickListener {
            onTableItemClick(getItem(position))
        }
        holder.itemBinding.run {
            tableName = getItem(position)
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TableItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTableBinding.inflate(
            inflater,
            parent,
            false
        )
        return TableItemViewHolder(binding)
    }

    override fun getItemCount(): Int = viewModel.tables.value?.size ?: 0
}
