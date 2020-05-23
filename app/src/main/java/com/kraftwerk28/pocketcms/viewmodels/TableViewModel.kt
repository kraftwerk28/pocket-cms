package com.kraftwerk28.pocketcms.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kraftwerk28.pocketcms.Database
import com.kraftwerk28.pocketcms.fragments.Cell
import com.kraftwerk28.pocketcms.toLists
import com.kraftwerk28.pocketcms.toMaps
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

// Only for LiveData table diff
typealias Row = MutableMap<String, Cell>

class TableViewModel(private val tableName: String) : ViewModel() {

    private val _modifiedRows = mutableMapOf<Int, Row>()
    private val _newRows = mutableListOf<Row>()
    private val _deletedRows = mutableSetOf<Int>()

    val rows = MutableLiveData<List<Map<String, Cell>>>(listOf())
    val header = MutableLiveData<List<String>>(listOf())
    val primaryKeys = MutableLiveData<List<String>>(listOf())
    val modifiedRows = MutableLiveData(_modifiedRows)
    val newRows = MutableLiveData(_newRows)
    val deletedRows = MutableLiveData(_deletedRows)
    val isLoading = MutableLiveData<Boolean>(false)
    val errorMessage = MutableLiveData<String?>(null)

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
        val newRow = (_modifiedRows[index] ?: rows.value!![index])
            .toMutableMap()
        newRow.putAll(header.value!!.fold(mutableMapOf()) { acc, col ->
            acc.set(col, Cell(update[col]))
            acc
        })
        _modifiedRows[index] = newRow
        modifiedRows.notify(_modifiedRows)
    }

    fun getCell(row: Int, col: Int): Cell? {
        val key = header.value!![col]
        if (_modifiedRows.containsKey(row)) {
            return _modifiedRows[row]!![key]
        } else {
            return rows.value!![row][key]
        }
    }

    fun modifyCell(row: Int, col: Int, value: Cell) {
        if (row < newRows.value!!.size) {
            val newRow = newRows.value!![row].toMutableMap()
            newRow.set(header.value!![col], value)
            _newRows[row].putAll(newRow)
            newRows.notify(_newRows)
        } else {
            val newRow = _modifiedRows[row] ?: mutableMapOf()
            newRow.set(header.value!![col], value)
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

    fun produceInsertQuery(): String? {
        if (newRows.value?.size == 0) return null
        fun rowMapper(row: Row): String =
            header.value!!.map { col -> row[col]?.let { "$it" } ?: "null" }
                .joinToString(prefix = "(", postfix = ")")
        return "INSERT INTO $tableName (${header.value!!.joinToString()}) " +
                "VALUES ${_newRows.map(::rowMapper).joinToString()}"
    }

    fun produceDeleteQuery(): String? {
        if (deletedRows.value?.size == 0) return null
        fun whereTuple(pk: String): String = _deletedRows
            .map { rows.value!![it][pk] }
            .joinToString(prefix = "(", postfix = ")")

        val whereClause = primaryKeys.value!!
            .map { pk -> "$pk IN ${whereTuple(pk)}" }
            .joinToString(" AND ")
        return "DELETE FROM $tableName" +
                " WHERE $whereClause"
    }

    fun produceUpdateQueries(): List<String> = listOf()

    fun report() {
        Log.i(javaClass.simpleName, _modifiedRows.toString())
        Log.i(javaClass.simpleName, _newRows.toString())
        Log.i(javaClass.simpleName, _deletedRows.toString())
    }

    fun fetchTable() = viewModelScope.launch {
        isLoading.value = true
        val head_sql = "SELECT column_name" +
                " FROM information_schema.columns" +
                " WHERE table_name = '$tableName'"
        val rows_sql = "SELECT * FROM $tableName LIMIT 20"
        val pk_sql = "SELECT column_name" +
                " FROM information_schema.key_column_usage" +
                " WHERE table_name = '$tableName'"

        val headers = Database
            .query(head_sql)
            .await()
            .toLists()
            .map { it[0].toString() }
        val rowsRes = Database
            .query(rows_sql)
            .await()
            .toMaps()
            .map { it.mapValues { Cell(it.value.toString()) }.toMutableMap() }
        val pks = Database
            .query(pk_sql)
            .await()
            .toLists()
            .map { it[0].toString() }

        this@TableViewModel.run {
            rows.value = rowsRes.toMutableList()
            header.value = headers
            primaryKeys.value = pks
        }

        isLoading.value = false
    }

    fun applyPatch() = viewModelScope.launch {
        isLoading.value = true

        produceInsertQuery()?.let {
            Database.update(it).await()
        }
        produceDeleteQuery()?.let {
            Database.update(it).await()
        }
        produceUpdateQueries().forEach {
            Database.update(it).await()
        }

        reset()
        fetchTable()
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
