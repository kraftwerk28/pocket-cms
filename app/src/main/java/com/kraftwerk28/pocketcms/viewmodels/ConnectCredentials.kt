package com.kraftwerk28.pocketcms.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

class ConnectCredentials(
    _host: String,
    _port: Int,
    _username: String,
    _password: String,
    _dbName: String
) : ViewModel() {

    val host = MutableLiveData<String>(_host)

    val port = MutableLiveData<Int>(_port)

    val username = MutableLiveData<String>(_username)

    val password = MutableLiveData<String>(_password)

    val dbName = MutableLiveData<String>(_dbName)

    val connString = MediatorLiveData<String>()

    init {
        val cb = Observer<Any> {
            connString.value =
                "postgresql://${host.value}:${port.value}/${dbName.value}"
        }
        connString.run {
            addSource(host, cb)
            addSource(port, cb)
            addSource(dbName, cb)
        }
    }
}
