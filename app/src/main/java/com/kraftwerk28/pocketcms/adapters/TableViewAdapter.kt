package com.kraftwerk28.pocketcms.fragments

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.kraftwerk28.pocketcms.R
import kotlinx.android.synthetic.main.item_table_cell.view.*

typealias CellChangeCallback = (row: Int, Column: Int, value: String) -> Unit

class TableViewAdapter(val onCellChange: CellChangeCallback) :
    AbstractTableAdapter<ColumnHeader, RowHeader, Cell>() {

    // On create methods
    override fun onCreateRowHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_table_cell, parent, false)
        return CellViewHolder(view, onCellChange)
    }

    override fun onCreateCellViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_table_cell, parent, false)
        return CellViewHolder(view, onCellChange)
    }

    override fun onCreateColumnHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_table_cell, parent, false)
        return CellViewHolder(view, onCellChange)
    }

    override fun onCreateCornerView(parent: ViewGroup): View =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_table_cell, parent, false)

    // On bind methods
    override fun onBindCellViewHolder(
        holder: AbstractViewHolder,
        cellItemModel: Cell?,
        columnPosition: Int,
        rowPosition: Int
    ) {
        (holder as CellViewHolder).bind(
            cellItemModel!!,
            rowPosition,
            columnPosition
        )
    }

    override fun onBindColumnHeaderViewHolder(
        holder: AbstractViewHolder,
        columnHeaderItemModel: ColumnHeader?,
        columnPosition: Int
    ) {
        (holder as CellViewHolder).bind(
            columnHeaderItemModel!!,
            -1,
            columnPosition
        )
    }

    override fun onBindRowHeaderViewHolder(
        holder: AbstractViewHolder,
        rowHeaderItemModel: RowHeader?,
        rowPosition: Int
    ) {
        (holder as CellViewHolder).bind(
            rowHeaderItemModel!!,
            rowPosition,
            -1
        )
    }


    // Type getters
    override fun getCellItemViewType(position: Int): Int = 0

    override fun getColumnHeaderItemViewType(position: Int): Int = 0

    override fun getRowHeaderItemViewType(position: Int): Int = 0
}

open class Cell(val data: String)

class ColumnHeader(data: String) : Cell(data)

class RowHeader(data: String) : Cell(data)

open class CellViewHolder(itemView: View, val listener: CellChangeCallback) :
    AbstractViewHolder(itemView) {
    fun bind(cell: Cell, row: Int, column: Int) {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) = Unit

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) = Unit

            override fun afterTextChanged(s: Editable?) {
                listener(row, column, s.toString())
            }
        }

        itemView.cellView.run {
            setText(cell.data)
            addTextChangedListener(textWatcher)
        }
    }
}
