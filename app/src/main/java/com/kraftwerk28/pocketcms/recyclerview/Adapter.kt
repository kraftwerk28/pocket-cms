package com.kraftwerk28.pocketcms.recyclerview

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kraftwerk28.pocketcms.R
import kotlinx.android.synthetic.main.list_item.view.*

class ListAdapter(private var viewModel: ListViewModel) :
    RecyclerView.Adapter<ListAdapter.ItemHolder>() {

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item,
            parent,
            false
        )
        return ItemHolder(item)
    }

    override fun getItemCount() = viewModel.taskCount

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val task = viewModel.getTask(position)!!

        holder.itemView.indexText.text = "$position."
        holder.itemView.dataText.text = task.description
        holder.itemView.dataText.paintFlags = if (task.done) {
            Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            0
        }
        holder.itemView.delButton.setOnClickListener {
            viewModel.delTask(position)
        }
        holder.itemView.setOnClickListener {
            viewModel.toggleTask(position)
        }
    }
}
