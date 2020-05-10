package com.kraftwerk28.pocketcms.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.kraftwerk28.pocketcms.R
import kotlinx.android.synthetic.main.item_table_cell.view.*

class TableViewAdapter : AbstractTableAdapter<ColumnHeader, RowHeader, Cell>() {

    override fun onCreateRowHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_table_cell, parent, false)
        return CellViewHolder(view)
    }

    override fun onCreateCellViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_table_cell, parent, false)
        return CellViewHolder(view)
    }

    override fun onCreateColumnHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_table_cell, parent, false)
        return CellViewHolder(view)
    }

    override fun onCreateCornerView(parent: ViewGroup): View {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_table_cell, parent, false)
        return view
    }


    override fun onBindCellViewHolder(
        holder: AbstractViewHolder,
        cellItemModel: Cell?,
        columnPosition: Int,
        rowPosition: Int
    ) {
        (holder as CellViewHolder).bind(cellItemModel!!)
    }

    override fun onBindColumnHeaderViewHolder(
        holder: AbstractViewHolder,
        columnHeaderItemModel: ColumnHeader?,
        columnPosition: Int
    ) {
        (holder as CellViewHolder).bind(columnHeaderItemModel!!)
    }

    override fun onBindRowHeaderViewHolder(
        holder: AbstractViewHolder,
        rowHeaderItemModel: RowHeader?,
        rowPosition: Int
    ) {
        (holder as CellViewHolder).bind(rowHeaderItemModel!!)
    }


    override fun getCellItemViewType(position: Int): Int = 0

    override fun getColumnHeaderItemViewType(position: Int): Int = 0

    override fun getRowHeaderItemViewType(position: Int): Int = 0
}

open class Cell(val data: String)

class ColumnHeader(data: String) : Cell(data)

class RowHeader(data: String) : Cell(data)

open class CellViewHolder(itemView: View) : AbstractViewHolder(itemView) {
    fun bind(cell: Cell) {
        itemView.cellView.setText(cell.data)
    }
}
