package com.kraftwerk28.pocketcms.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kraftwerk28.pocketcms.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DBViewModel : ViewModel() {
    val dbList = MutableLiveData<MutableList<String>>(mutableListOf())

    fun fetchDbList() = viewModelScope.launch {
        dbList.value?.clear()
        dbList.value = dbList.value
        val dbs = Database
            .query("SELECT datname FROM pg_database WHERE datname !~* '^template'")
            .await()
        dbList.value?.addAll(dbs.toMaps().map { it.get("datname").toString() })
        dbList.value = dbList.value
    }

    fun removeDB(dbName: String) = viewModelScope.async {
        Database.query("DROP DATABASE $dbName").await().succeed()
    }
}
