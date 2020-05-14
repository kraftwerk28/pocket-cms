package com.kraftwerk28.pocketcms.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.kraftwerk28.pocketcms.R
import com.kraftwerk28.pocketcms.viewmodels.TableDiff
import com.kraftwerk28.pocketcms.viewmodels.TableViewModel
import kotlinx.android.synthetic.main.fragment_table_view.view.*

class TableViewFragment : Fragment() {
    val headerMockData = (0..9).map { ColumnHeader("H:$it") }
    val cellMockData = (0..9).map { row ->
        headerMockData.foldIndexed(
            mutableMapOf<String, Cell>(),
            { idx, acc, h ->
                acc.set(h.data, Cell("$row:$idx"))
                acc
            })
    }

    //    val headerMockData = mockData.get(0).map { ColumnHeader("H:${it.data}") }
    private lateinit var viewModel: TableViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val inflated = inflater
            .inflate(R.layout.fragment_table_view, container, false)
        viewModel = TableViewModel()
        viewModel.rows.value = cellMockData
        viewModel.header.value = headerMockData

        val adapter = TableViewAdapter(requireContext(), viewModel)

        inflated.run {
            tableView.setAdapter(adapter)
        }

        viewModel.tableDiff.observe(viewLifecycleOwner, Observer {
//            adapter.updatedRows = it.modified.keys.toList()
//            adapter.setCellItems(
//                it.new.map {
//                    it.values.toList().map { Cell(it) }
//                }.plus(it.modified.values.map {
//                    it.values.toList().map { Cell(it) }
//                })
//            )
        })

//        adapter.setRows(headerMockData, cellMockData)

        inflated.run {
            toolbar.setOnClickListener {
                findNavController().navigateUp()
            }
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.new_row -> {
                        viewModel.updateTableDiff {
                            it.newRow()
                            it.updateExistingRow(3, Pair("kek", "lol"))
                        }
                        true
                    }
                    else -> false
                }
            }
        }

        return inflated
    }

}
