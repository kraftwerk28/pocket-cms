package com.kraftwerk28.pocketcms.fragments

import android.content.Context
import android.graphics.ColorFilter
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.kraftwerk28.pocketcms.R
import com.kraftwerk28.pocketcms.viewmodels.TableViewModel
import kotlinx.android.synthetic.main.item_table_cell.view.*

class TableViewAdapter(
    val context: Context,
    val viewModel: TableViewModel
) : AbstractTableAdapter<ColumnHeader, RowHeader, Cell>() {

    // On create methods
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
        val h = holder as CellViewHolder
        h.bind(
            cellItemModel!!,
            rowPosition,
            columnPosition
        )

        if (viewModel.tableDiff.value?.modified?.contains(rowPosition)
                ?: false
        ) {
            h.itemView.cellView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.table_cell_updated
                )
            )
        }
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

    fun setRows(header: List<ColumnHeader>, data: List<Map<String, Cell>>) {
        setColumnHeaderItems(header)
        setCellItems(
            data.map { cortage -> header.map { cortage.get(it.data) } }
        )
    }

    fun notifyRowsChanged() {

    }
}

open class Cell(val data: String)

class ColumnHeader(data: String) : Cell(data)

class RowHeader(data: String) : Cell(data)

open class CellViewHolder(itemView: View) :
    AbstractViewHolder(itemView) {
    fun bind(cell: Cell, row: Int, column: Int) {
        itemView.cellView.run {
            text = cell.data
            setOnClickListener {
                Log.i(javaClass.simpleName, "Row $row clicked")
            }
        }
    }
}
