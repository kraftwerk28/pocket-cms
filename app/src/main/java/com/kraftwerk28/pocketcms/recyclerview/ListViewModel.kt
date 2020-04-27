package com.kraftwerk28.pocketcms.recyclerview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ListViewModel : ViewModel() {
    data class ToDoTask(val description: String, var done: Boolean)

    val label = MutableLiveData<String>()
    val _taskList: MutableList<ToDoTask> = mutableListOf()
    val taskList = MutableLiveData<MutableList<ToDoTask>>()
    val taskCount
        get() = taskList.value?.size ?: 0

    fun addTask(text: String) {
        label.value = text
        val t = ToDoTask(text, false)
        _taskList.add(0, t)
        taskList.value = _taskList
    }

    fun delTask(position: Int) {
        _taskList.removeAt(position)
        taskList.value = _taskList
    }

    fun getTask(position: Int) = taskList.value?.get(position)

    fun toggleTask(position: Int) {
        val t = _taskList.get(position)
        t.done = !t.done
        _taskList.set(position, t)
        taskList.value = _taskList
    }
}
