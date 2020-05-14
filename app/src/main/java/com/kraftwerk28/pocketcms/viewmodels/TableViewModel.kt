package com.kraftwerk28.pocketcms.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kraftwerk28.pocketcms.fragments.Cell
import com.kraftwerk28.pocketcms.fragments.ColumnHeader

typealias RowData = MutableMap<String, String>

class TableDiff() {
    val new: MutableList<RowData> = mutableListOf()
    val modified: MutableMap<Int, RowData> = mutableMapOf()
    val deleted: MutableSet<Int> = mutableSetOf()

    fun newRow() {
        new.add(mutableMapOf())
    }

    fun updateNewRow(index: Int, update: Pair<String, String>) {
        val oldRow = HashMap(new.get(index))
        oldRow.set(update.first, update.second)
        new.set(index, oldRow)
    }

    fun updateExistingRow(index: Int, update: Pair<String, String>) {
        val oldRow = modified.get(index) ?: mutableMapOf()
        oldRow.set(update.first, update.second)
        modified.set(index, oldRow)
    }

    fun deleteRow(index: Int) {
        deleted.add(index)
    }
}

class TableViewModel : ViewModel() {
    val rows = MutableLiveData<List<MutableMap<String, Cell>>>(
        mutableListOf()
    )
    val header = MutableLiveData<List<ColumnHeader>>(mutableListOf())
    val tableDiff = MutableLiveData(TableDiff())

    fun updateTableDiff(callback: (td: TableDiff) -> Unit) {
        callback(tableDiff.value!!)
        tableDiff.notify()
    }
}
