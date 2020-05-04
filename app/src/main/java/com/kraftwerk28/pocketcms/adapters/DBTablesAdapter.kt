package com.kraftwerk28.pocketcms.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kraftwerk28.pocketcms.databinding.ItemTableBinding
import com.kraftwerk28.pocketcms.viewmodels.DBTablesViewModel

class DBTablesAdapter(val viewModel: DBTablesViewModel) :
    RecyclerView.Adapter<DBTablesAdapter.ViewHolder>() {

    class ViewHolder(val itemBinding: ItemTableBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = viewModel.tables.value!![position]
        holder.itemBinding.tableName = data
        holder.itemBinding.executePendingBindings()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTableBinding.inflate(
            inflater,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = viewModel.tables.value?.size ?: 0
}
