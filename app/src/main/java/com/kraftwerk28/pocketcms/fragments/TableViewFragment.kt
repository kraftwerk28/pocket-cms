package com.kraftwerk28.pocketcms.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.evrencoskun.tableview.TableView
import com.kraftwerk28.pocketcms.R
import kotlinx.android.synthetic.main.fragment_table_view.*
import kotlinx.android.synthetic.main.fragment_table_view.view.*

class TableViewFragment : Fragment() {
    val mockData =
        (1..10).map { row -> (1..10).map { col -> Cell("$row:$col") } }
    val headerMockData = mockData.get(0).map { ColumnHeader("H:$it") }
    val rowHeaderMockData =
        mockData.map { it.get(0) }.map { RowHeader("R:$it") }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val inflated = inflater
            .inflate(R.layout.fragment_table_view, container, false)
        val adapter = TableViewAdapter()

        inflated.run {
            tableView.setAdapter(adapter)
        }

        adapter.setCellItems(mockData)

        inflated.run {
            toolbar.setOnClickListener {
                findNavController().navigateUp()
            }
        }
//        mockData.forEach { row ->
//            val tableRow = TableRow(context)
//            row.forEach { cell ->
//                val tv = TextView(context)
//                tv.setPadding(32)
//                tableRow.addView(tv)
//            }
//            view.tableView.addView(tableRow)
//        }
        return inflated
    }

}
