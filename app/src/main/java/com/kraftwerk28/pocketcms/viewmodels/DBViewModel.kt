package com.kraftwerk28.pocketcms.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kraftwerk28.pocketcms.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DBViewModel : ViewModel() {
    val dbList = MutableLiveData<MutableList<String>>(mutableListOf())

    fun fetchDbList() = viewModelScope.launch {
        delay(2000)
        dbList.value?.clear()
        dbList.value = dbList.value
        val dbs = Database
            .query("SELECT datname FROM pg_database")
            .await()
        dbList.value?.addAll(dbs.toMaps().map { it.get("datname").toString() })
        dbList.value = dbList.value
    }
}
