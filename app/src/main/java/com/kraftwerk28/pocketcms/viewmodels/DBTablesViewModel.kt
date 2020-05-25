package com.kraftwerk28.pocketcms.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kraftwerk28.pocketcms.Database
import com.kraftwerk28.pocketcms.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DBTablesViewModel : ViewModel() {

    val tables = MutableLiveData(listOf<String>())
    val isLoading = MutableLiveData(false)

    fun fetchTables() = viewModelScope.launch {
        isLoading.value = true
        tables.value = listOf()
        delay(1000)
        val res = Database
            .query("SELECT * FROM pg_tables WHERE tablename !~* '^(sql_|pg_)'")
            .await()
        tables.value = res
            .toMaps()
            .map { (it.get("tablename") ?: "null").toString() }
        isLoading.value = false
    }

    fun removeTable(tableName: String) = viewModelScope.launch {
        val sql = "DROP TABLE $tableName"
        Database.update(sql).await()
        fetchTables()
    }
}
