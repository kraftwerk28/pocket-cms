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

    val _tables = MutableLiveData(listOf<String>())

    val tables: LiveData<List<String>> = _tables

    fun fetchTables() = viewModelScope.launch {
        _tables.value = listOf()
        delay(1000)
        val res = Database
            .query("SELECT * FROM pg_tables WHERE tablename !~* '^(sql_|pg_)'")
            .await()
        _tables.value =
            res.toMaps().map { (it.get("tablename") ?: "null").toString() }
        Log.i(javaClass.simpleName, _tables.value.toString())
    }
}
