package com.kraftwerk28.pocketcms

import android.app.AlertDialog
import android.content.Context
import android.provider.ContactsContract
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.evrencoskun.tableview.listener.ITableViewListener
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
            getCell(row, column)?.let { oldCell ->
                if (deletedRows.value!!.contains(row)) return
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
        val etext =
            com.google.android.material.textfield.TextInputEditText(context)
        etext.setText(oldCell.data)
        AlertDialog
            .Builder(context)
            .setView(etext)
            .setTitle("Edit data")
            .setPositiveButton("Ok") { dialog, which ->
                modifyCallback(Cell(etext.text.toString()))
            }
            .setNegativeButton("Delete row") { dialog, which ->
                deleteCallback()
            }
            .show()
    }
}