package com.kraftwerk28.pocketcms.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.kraftwerk28.pocketcms.Database
import com.kraftwerk28.pocketcms.R
import com.kraftwerk28.pocketcms.adapters.DBListAdapter
import com.kraftwerk28.pocketcms.dialogs.CreateDatabaseDialog
import com.kraftwerk28.pocketcms.viewmodels.DBViewModel
import com.kraftwerk28.pocketcms.viewmodels.notify
import kotlinx.android.synthetic.main.dbview_item.view.*
import kotlinx.android.synthetic.main.fragment_dbconnect.view.*
import kotlinx.android.synthetic.main.fragment_dbview.view.*
import kotlinx.coroutines.*

class DBViewFragment : Fragment() {

    private lateinit var adapter: DBListAdapter
    private lateinit var inflated: View
    private lateinit var viewModel: DBViewModel
    private val itemTouchHelperCallback =
        object :
            ItemTouchHelper.SimpleCallback(0, LEFT or RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {
                val dbName = viewHolder.itemView.dbnameText.text.toString()
                val pos = viewModel.dbList.value?.indexOf(dbName) ?: -1
                if (pos == -1) return
                confirmAction("Remove database $dbName?", {
                    viewModel.removeDB(dbName)
                }, {
                    adapter.notifyItemChanged(pos)
                })
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreateView(inflater, container, savedInstanceState)
        inflated = inflater.inflate(R.layout.fragment_dbview, container, false)

        viewModel = DBViewModel()

        activity?.actionBar?.title = "Databases"
        adapter = DBListAdapter(onDBItemClick = {
            GlobalScope.launch(Dispatchers.IO) {
                Database.credentials = Database.credentials?.copy(dbName = it)
                Database.connect().await()
                findNavController().navigate(R.id.action_DBViewFragment_to_DBTablesViewFragment)
            }
        })

        inflated.run {
            dbListView.adapter = adapter
            refreshLayout.setOnRefreshListener {
                viewModel.fetchDbList()
            }
            createDatabaseFAB.setOnClickListener {
                CreateDatabaseDialog {
                    viewModel.addDB(it)
                }.show(parentFragmentManager, "create database dialog")
            }
            ItemTouchHelper(itemTouchHelperCallback)
                .attachToRecyclerView(dbListView)
        }

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            setListLoading(it)
        })
        viewModel.dbList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it.toList())
        })

        viewModel.fetchDbList()

        return inflated
    }

    fun setListLoading(loading: Boolean = true) {
        inflated.refreshLayout.isRefreshing = loading
    }

    fun confirmAction(
        title: String,
        okCallback: () -> Unit,
        cancelCallback: () -> Unit
    ) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setPositiveButton("Yes") { _, _ -> okCallback() }
            .setNegativeButton("Cancel") { _, _ -> cancelCallback() }
            .show()
    }

}
