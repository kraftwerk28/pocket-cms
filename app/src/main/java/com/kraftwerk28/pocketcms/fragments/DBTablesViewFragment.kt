package com.kraftwerk28.pocketcms.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.kraftwerk28.pocketcms.dialogs.ConfirmAction
import com.kraftwerk28.pocketcms.Database
import com.kraftwerk28.pocketcms.ItemSwipeHelper
import com.kraftwerk28.pocketcms.R
import com.kraftwerk28.pocketcms.adapters.DBTablesAdapter
import com.kraftwerk28.pocketcms.viewmodels.DBTablesViewModel
import kotlinx.android.synthetic.main.dbview_item.view.*
import kotlinx.android.synthetic.main.fragment_dbtables_view.view.*

class DBTablesViewFragment : Fragment() {

    private lateinit var adapter: DBTablesAdapter
    lateinit var viewModel: DBTablesViewModel
    private val itemTouchHelperCallback = ItemSwipeHelper {
        val tableName = it.itemView.dbnameText.text.toString()
        val pos = viewModel.tables.value?.indexOf(tableName) ?: -1
        if (pos == -1) return@ItemSwipeHelper
        ConfirmAction(
            requireContext(),
            "Remove table $tableName?",
            {
                viewModel.removeTable(tableName)
            },
            {
                adapter.notifyItemChanged(pos)
            }
        ).invoke()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val inflated = inflater.inflate(
            R.layout.fragment_dbtables_view,
            container,
            false
        )

        viewModel = DBTablesViewModel()
        adapter = DBTablesAdapter(
            viewModel,
            onTableItemClick = {
                Database.currentTable = it
                findNavController()
                    .navigate(R.id.action_DBTables_to_TableView)
            }
        )

        requireActivity().run {
            onBackPressedDispatcher.addCallback {
                findNavController()
                    .navigate(R.id.action_DBTables_to_DBConnect)
            }
        }

        inflated.run {
            dbTablesView.adapter = adapter
            ItemTouchHelper(itemTouchHelperCallback)
                .attachToRecyclerView(dbTablesView)

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.fetchTables()
            }

            viewModel.tables.observe(
                viewLifecycleOwner,
                Observer {
                    adapter.submitList(it)
                }
            )
            viewModel.isLoading.observe(viewLifecycleOwner, Observer {
                swipeRefreshLayout.isRefreshing = it
            })

            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        return inflated
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchTables()
    }
}
