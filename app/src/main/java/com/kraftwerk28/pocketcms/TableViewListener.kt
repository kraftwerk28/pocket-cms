package com.kraftwerk28.pocketcms

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.evrencoskun.tableview.listener.ITableViewListener
import com.kraftwerk28.pocketcms.dialogs.ModifyCellDialog
import com.kraftwerk28.pocketcms.fragments.Cell
import com.kraftwerk28.pocketcms.viewmodels.TableViewModel

class TableViewListener(
    val context: Context,
    val viewModel: TableViewModel
) :
    ITableViewListener {

    override fun onCellLongPressed(
        cellView: RecyclerView.ViewHolder,
        column: Int,
        row: Int
    ) {
    }

    override fun onColumnHeaderLongPressed(
        columnHeaderView: RecyclerView.ViewHolder,
        column: Int
    ) {
    }

    override fun onRowHeaderClicked(
        rowHeaderView: RecyclerView.ViewHolder,
        row: Int
    ) {
    }

    override fun onColumnHeaderClicked(
        columnHeaderView: RecyclerView.ViewHolder,
        column: Int
    ) {
    }

    override fun onCellClicked(
        cellView: RecyclerView.ViewHolder,
        column: Int,
        row: Int
    ) {
        viewModel.run {
            if (deletedRows.value!!.contains(row - newRows.value!!.size)) return
            getCell(row, column)?.let { oldCell ->
                promptForValue(oldCell, { newCell ->
                    modifyCell(row, column, newCell)
                }, {
                    deleteRow(row)
                })
            }
        }
    }

    override fun onColumnHeaderDoubleClicked(
        columnHeaderView: RecyclerView.ViewHolder,
        column: Int
    ) {
    }

    override fun onCellDoubleClicked(
        cellView: RecyclerView.ViewHolder,
        column: Int,
        row: Int
    ) {
    }

    override fun onRowHeaderLongPressed(
        rowHeaderView: RecyclerView.ViewHolder,
        row: Int
    ) {
    }

    override fun onRowHeaderDoubleClicked(
        rowHeaderView: RecyclerView.ViewHolder,
        row: Int
    ) {
    }

    private fun promptForValue(
        oldCell: Cell,
        modifyCallback: (newCell: Cell) -> Unit,
        deleteCallback: () -> Unit
    ) {
        ModifyCellDialog(context, oldCell, modifyCallback, deleteCallback)
            .invoke()
    }
}