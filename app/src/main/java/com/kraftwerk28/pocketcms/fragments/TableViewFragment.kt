package com.kraftwerk28.pocketcms.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.Px
import androidx.core.view.setPadding

import com.kraftwerk28.pocketcms.R
import kotlinx.android.synthetic.main.fragment_table_view.*
import kotlinx.android.synthetic.main.fragment_table_view.view.*

class TableViewFragment : Fragment() {
    val mockData = (1..10).map { row -> (1..10).map { col -> "$row:$col" } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =
            inflater.inflate(R.layout.fragment_table_view, container, false)
        mockData.forEach { row ->
            val tableRow = TableRow(context)
            row.forEach { cell ->
                val tv = TextView(context)
                tv.setPadding(32)
                tableRow.addView(tv)
            }
            view.tableView.addView(tableRow)
        }
        return view
    }

    fun createTable() {
        mockData.forEach { row ->
            val tableRow = TableRow(context)
            row.forEach { cell ->
                val tv = TextView(context)
                tv.setPadding(32)
                tableRow.addView(tv)
            }
            tableView.addView(tableRow)
        }
    }
}
