package com.kraftwerk28.pocketcms.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kraftwerk28.pocketcms.Database
import com.kraftwerk28.pocketcms.fragments.Cell
import com.kraftwerk28.pocketcms.toLists
import com.kraftwerk28.pocketcms.toMaps
import kotlinx.coroutines.launch

// Only for LiveData table diff
typealias Row = MutableMap<String, Cell>

class TableViewModel(
    var header: List<String>,
    var rows: List<Map<String, Cell>>,
    private val primaryKeys: List<String>,
    private val tableName: String
) : ViewModel() {
    private val _modifiedRows = mutableMapOf<Int, Row>()
    private val _newRows = mutableListOf<Row>()
    private val _deletedRows = mutableSetOf<Int>()

    val modifiedRows = MutableLiveData(_modifiedRows)
    val newRows = MutableLiveData(_newRows)
    val deletedRows = MutableLiveData(_deletedRows)
    var isLoading = MutableLiveData<Boolean>(false)

    fun newRow() {
        _newRows.add(mutableMapOf())
        newRows.notify(_newRows)
    }

    fun updateNewRow(index: Int, update: Pair<String, String>) {
        val newRow = _newRows.get(index).toMutableMap()
        newRow.set(update.first, Cell(update.second))
        _newRows.set(index, newRow)
        newRows.notify(_newRows)
    }

    fun modifyRow(index: Int, update: Pair<String, String>) {
        val newRow = _newRows.get(index).toMutableMap()
        newRow.set(update.first, Cell(update.second))
        _modifiedRows.set(index, newRow)
        modifiedRows.notify(_modifiedRows)
    }

    fun updateExistingRow(index: Int, update: Pair<String, Cell>) {
        val newRow = (
                _modifiedRows.get(index) ?: mutableMapOf()
                ).toMutableMap()
        newRow[update.first] = update.second
        _modifiedRows[index] = newRow
        modifiedRows.notify(_modifiedRows)
    }

    fun updateExistingRow(index: Int, update: Map<String, Cell>) {
        val newRow = (_modifiedRows[index] ?: rows[index])
            .toMutableMap()
        newRow.putAll(header.fold(mutableMapOf()) { acc, col ->
            acc.set(col, Cell(update[col]))
            acc
        })
        _modifiedRows[index] = newRow
        modifiedRows.notify(_modifiedRows)
    }

    fun getCell(row: Int, col: Int): Cell? {
        val key = header[col]
        if (_modifiedRows.containsKey(row)) {
            return _modifiedRows[row]!![key]
        } else {
            return rows[row][key]
        }
    }

    fun modifyCell(row: Int, col: Int, value: Cell) {
        if (row < newRows.value!!.size) {
            val newRow = newRows.value!![row].toMutableMap()
            newRow.set(header[col], value)
            _newRows[row].putAll(newRow)
            newRows.notify(_newRows)
        } else {
            val newRow = _modifiedRows[row] ?: mutableMapOf()
            newRow.set(header[col], value)
            if (!_modifiedRows.containsKey(row)) {
                _modifiedRows.set(row, mutableMapOf())
            }
            _modifiedRows[row]?.putAll(newRow)
            modifiedRows.notify(_modifiedRows)
            report()
        }
    }

    fun deleteRow(index: Int) {
        _deletedRows.add(index)
        deletedRows.notify(_deletedRows)
    }

    fun produceInsertQuery(): String {
        fun rowMapper(row: Row): String =
            header.map { col -> row[col]?.let { "'$it'" } ?: "null" }
                .joinToString(prefix = "(", postfix = ")")
        return "INSERT INTO $tableName (${header.joinToString()}) " +
                "VALUES ${_newRows.map(::rowMapper).joinToString()}"
    }

    fun produceDeleteQuery(): String {
        fun whereTuple(pk: String): String = _deletedRows
            .map { rows.get(it).get(pk) }
            .joinToString(prefix = "(", postfix = ")")

        val whereClause = primaryKeys
            .map { pk -> "$pk IN ${whereTuple(pk)}" }
            .joinToString(" AND ")
        return "DELETE FROM $tableName " +
                "WHERE $whereClause"
    }

    fun report() {
        Log.i(javaClass.simpleName, _modifiedRows.toString())
        Log.i(javaClass.simpleName, _newRows.toString())
        Log.i(javaClass.simpleName, _deletedRows.toString())
    }

    fun fetchTable() = viewModelScope.launch {
        isLoading.value = true
        val head_sql = "SELECT column_name FROM information_schema.columns" +
                "WHERE table_name = $tableName"
        val rows_sql = "SELECT * FROM $tableName LIMIT 20"

        val headers = Database
            .query(head_sql)
            .await()
            .toLists()
            .map { it[0].toString() }
        val rowsRes = Database
            .query(rows_sql)
            .await()
            .toMaps()
            .map { it.mapValues { Cell(it.toString()) }.toMutableMap() }

        this@TableViewModel.rows = rowsRes.toMutableList()
        this@TableViewModel.header = headers

        isLoading.value = false
    }

    fun applyPatch() = viewModelScope.launch {
        isLoading.value = true
        Database.update(produceInsertQuery()).await()
        Database.update(produceDeleteQuery()).await()
        reset()
        isLoading.value = false
    }

    fun reset() {
        _modifiedRows.run {
            clear()
            modifiedRows.notify(this)
        }
        _newRows.run {
            clear()
            newRows.notify(this)
        }
        _deletedRows.run {
            clear()
            deletedRows.notify(this)
        }
    }

}
