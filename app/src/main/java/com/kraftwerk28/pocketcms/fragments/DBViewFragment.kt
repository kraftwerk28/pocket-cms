package com.kraftwerk28.pocketcms.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.kraftwerk28.pocketcms.ConfirmAction
import com.kraftwerk28.pocketcms.Database
import com.kraftwerk28.pocketcms.ItemSwipeHelper
import com.kraftwerk28.pocketcms.R
import com.kraftwerk28.pocketcms.adapters.DBListAdapter
import com.kraftwerk28.pocketcms.dialogs.CreateDatabaseDialog
import com.kraftwerk28.pocketcms.viewmodels.DBViewModel
import kotlinx.android.synthetic.main.dbview_item.view.*
import kotlinx.android.synthetic.main.fragment_dbview.view.*
import kotlinx.coroutines.*

class DBViewFragment : Fragment() {

    private lateinit var adapter: DBListAdapter
    private lateinit var inflated: View
    private lateinit var viewModel: DBViewModel
    private val itemTouchHelperCallback = ItemSwipeHelper {
        val dbName = it.itemView.dbnameText.text.toString()
        val pos = viewModel.dbList.value?.indexOf(dbName) ?: -1
        if (pos == -1) return@ItemSwipeHelper
        ConfirmAction(requireContext(), "Remove database $dbName?", {
            viewModel.removeDB(dbName)
        }, {
            adapter.notifyItemChanged(pos)
        }).invoke()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreateView(inflater, container, savedInstanceState)
        inflated = inflater.inflate(R.layout.fragment_dbview, container, false)

        viewModel = DBViewModel()

        adapter = DBListAdapter(onDBItemClick = {
            GlobalScope.launch(Dispatchers.Main) {
                Database.run {
                    credentials = Database.credentials?.copy(dbName = it)
                    connect().await()
                }
                findNavController().navigate(
                    R.id.action_DBViewFragment_to_DBTablesViewFragment
                )
            }
        })

        inflated.run {
            dbListView.adapter = adapter
            refreshLayout.setOnRefreshListener {
                viewModel.fetchDbList()
            }
            createDatabaseFAB.setOnClickListener {
                CreateDatabaseDialog(requireActivity())
                { viewModel.addDB(it) }
                    .show()
            }
            ItemTouchHelper(itemTouchHelperCallback)
                .attachToRecyclerView(dbListView)
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
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

}
