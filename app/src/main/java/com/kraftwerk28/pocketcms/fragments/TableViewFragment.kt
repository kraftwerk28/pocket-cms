package com.kraftwerk28.pocketcms.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.kraftwerk28.pocketcms.Database
import androidx.recyclerview.widget.RecyclerView as RV
import com.kraftwerk28.pocketcms.R
import com.kraftwerk28.pocketcms.TableViewListener
import com.kraftwerk28.pocketcms.viewmodels.TableViewModel
import kotlinx.android.synthetic.main.fragment_dbview.view.*
import kotlinx.android.synthetic.main.fragment_table_view.*
import kotlinx.android.synthetic.main.fragment_table_view.view.*
import kotlinx.android.synthetic.main.fragment_table_view.view.refreshLayout
import kotlinx.android.synthetic.main.fragment_table_view.view.toolbar

class TableViewFragment : Fragment() {
//    val headerMockData = (0..9).map { "H:$it" }
//    val cellMockData = (0..9).map { row ->
//        headerMockData.foldIndexed(
//            mutableMapOf<String, Cell>(),
//            { idx, acc, h ->
//                acc.set(h, Cell("$row:$idx"))
//                acc
//            })
//    }

    //    val headerMockData = mockData.get(0).map { ColumnHeader("H:${it.data}") }
    private lateinit var viewModel: TableViewModel
    private lateinit var tableAdapter: TableViewAdapter

    //
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val inflated = inflater
            .inflate(R.layout.fragment_table_view, container, false)
        viewModel = TableViewModel(Database.currentTable!!)
//        viewModel.rows = cellMockData.toMutableList()
//        viewModel.header = headerMockData

        tableAdapter = TableViewAdapter(requireContext(), viewModel)
        val tableViewListener = TableViewListener(requireContext(), viewModel)

        viewModel.run {
            modifiedRows.observe(viewLifecycleOwner, Observer {
                tableAdapter.updateTableContents()
            })
            deletedRows.observe(viewLifecycleOwner, Observer {
                tableAdapter.updateTableContents()
            })

            // TODO: use adapter helper functions
            newRows.observe(viewLifecycleOwner, Observer {
                tableAdapter.updateTableContents()
            })
            rows.observe(viewLifecycleOwner, Observer {
                tableAdapter.updateTableContents()
            })

            isLoading.observe(viewLifecycleOwner, Observer {
                refreshLayout.isRefreshing = it
            })

            errorMessage.observe(viewLifecycleOwner, Observer {
                it?.let {
                    // Show dialog with error
                }
            })
        }

        inflated.run {
            tableView.run {
                setAdapter(tableAdapter)
                setTableViewListener(tableViewListener)
            }

            toolbar.setOnClickListener {
                findNavController().navigateUp()
            }
            toolbar.setOnMenuItemClickListener {
                onMenuItemClick(it)
            }

            fabUpload.setOnClickListener {
                viewModel.applyPatch()
            }
            refreshLayout.setOnRefreshListener {
                onRefresh()
            }
        }

        viewModel.fetchTable()

        return inflated
    }

    fun onRefresh() {
        viewModel.fetchTable()
        tableAdapter.updateTableContents()
        requireView().refreshLayout.isRefreshing = false
    }

    fun onMenuItemClick(item: MenuItem): Boolean = when (item.itemId) {
        R.id.newRow -> {
            viewModel.newRow()
            true
        }
        R.id.reset -> {
            viewModel.reset()
            true
        }
        else -> false
    }

}
