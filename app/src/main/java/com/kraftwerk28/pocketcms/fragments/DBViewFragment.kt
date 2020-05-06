package com.kraftwerk28.pocketcms.fragments

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
                val dialog = CreateDatabaseDialog()
                dialog.show(parentFragmentManager, "create database dialog")
            }
            ItemTouchHelper(itemTouchHelperCallback)
                .attachToRecyclerView(dbListView)
        }

        viewModel.fetchDbList()

        viewModel.dbList.observe(this, Observer {
            Log.i(tag, it?.toString() ?: run { "nothing" })
            if (it.size == 0) {
                setListLoading()
            } else {
                setListLoading(false)
                adapter.dbList = it
                adapter.notifyDataSetChanged()
            }
        })

        return inflated
    }

    fun setListLoading(loading: Boolean = true) {
        inflated.refreshLayout.isRefreshing = loading
    }

}
