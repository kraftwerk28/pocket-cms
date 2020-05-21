package com.kraftwerk28.pocketcms.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kraftwerk28.pocketcms.R
import kotlinx.android.synthetic.main.item_modify_row.view.*

class RowItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class RowItemDiffCallback : DiffUtil.ItemCallback<Pair<String, String>>() {
    override fun areItemsTheSame(
        oldItem: Pair<String, String>,
        newItem: Pair<String, String>
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun areContentsTheSame(
        oldItem: Pair<String, String>,
        newItem: Pair<String, String>
    ): Boolean {
        TODO("Not yet implemented")
    }
}

class ModifyRowDialogAdapter :
    ListAdapter<Pair<String, String>, RowItemHolder>(RowItemDiffCallback()) {
    override fun onBindViewHolder(holder: RowItemHolder, position: Int) {
        val data = getItem(position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RowItemHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_modify_row, parent, false)
        return RowItemHolder(view)
    }
}