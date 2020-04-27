package com.kraftwerk28.pocketcms

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ListView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.kraftwerk28.pocketcms.recyclerview.ListAdapter
import com.kraftwerk28.pocketcms.recyclerview.ListViewModel
import kotlinx.android.synthetic.main.activity_recycler.*

class RecyclerActivity : AppCompatActivity() {

    val TAG = "Recycler activity"
    lateinit var viewModel: ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)

        viewModel = ListViewModel()
        val adapter = ListAdapter(viewModel)

        listView.adapter = adapter
        viewModel.taskList.observe(this, Observer {
            Log.i(TAG, it.toString())
            adapter.notifyDataSetChanged()
        })
        viewModel.label.observe(this, Observer {
            labelText.text = it
        })

        taskText.setOnEditorActionListener { v, actionId, event ->
            when {
                v.text.trim().length == 0 -> {
                    v.text = ""
                    true
                }
                actionId == EditorInfo.IME_ACTION_DONE -> {
                    viewModel.addTask(v.text.toString())
                    v.text = ""
                    closeKbd(v)
                    true
                }
                else -> true
            }
        }
    }

    fun closeKbd(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
