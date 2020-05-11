package com.kraftwerk28.pocketcms.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.kraftwerk28.pocketcms.R
import kotlinx.android.synthetic.main.fragment_table_view.view.*

class TableViewFragment : Fragment() {
    val mockData =
        (1..10).map { row -> (1..10).map { col -> Cell("$row:$col") } }
    val headerMockData = mockData.get(0).map { ColumnHeader("H:${it.data}") }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val inflated = inflater
            .inflate(R.layout.fragment_table_view, container, false)

        val adapter = TableViewAdapter({ row, column, value ->
            Log.i("TableView", "$row:$column $value")
        })

        inflated.run {
            tableView.setAdapter(adapter)
        }

        adapter.setCellItems(mockData)
        adapter.setColumnHeaderItems(headerMockData)

        inflated.run {
            toolbar.setOnClickListener {
                findNavController().navigateUp()
            }
        }

        return inflated
    }

}
