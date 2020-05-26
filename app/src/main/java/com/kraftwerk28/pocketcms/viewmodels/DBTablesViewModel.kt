package com.kraftwerk28.pocketcms.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kraftwerk28.pocketcms.Database
import com.kraftwerk28.pocketcms.*
import com.kraftwerk28.pocketcms.dialogs.TableColumn
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.sql.SQLException

class DBTablesViewModel : ViewModel() {

    private val _tables = mutableListOf<String>()
    val tables = MutableLiveData(_tables.toList())
    val isLoading = MutableLiveData(false)
    val errorText = MutableLiveData<String?>(null)

    fun fetchTables() = viewModelScope.launch {
        isLoading.value = true
        delay(1000)
        val res = Database
            .query("SELECT * FROM pg_tables WHERE tablename !~* '^(sql_|pg_)'")
            .await()
        _tables.clear()
        _tables.addAll(
            res
                .toMaps()
                .map { (it.get("tablename") ?: "null").toString() }
        )
        tables.value = _tables.toList()
        isLoading.value = false
    }

    fun removeTable(tableName: String) = viewModelScope.launch {
        val sql = "DROP TABLE $tableName"
        Database.update(sql).await()
        fetchTables()
    }

    fun createTable(tableName: String, columnList: List<TableColumn>) =
        viewModelScope.launch {
            val sql = "CREATE TABLE $tableName " +
                    columnList.joinToString(
                        prefix = "(",
                        postfix = ")"
                    ) { "${it.value} ${it.type}" }
            try {
                Database.update(sql).await()
            } catch (e: SQLException) {
                errorText.value = e.toString()
            }
            fetchTables()
        }
}
