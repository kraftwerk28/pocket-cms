package com.kraftwerk28.pocketcms.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.kraftwerk28.pocketcms.ConfirmAction
import com.kraftwerk28.pocketcms.Database
import com.kraftwerk28.pocketcms.ItemSwipeHelper
import com.kraftwerk28.pocketcms.R
import com.kraftwerk28.pocketcms.adapters.DBTablesAdapter
import com.kraftwerk28.pocketcms.databinding.FragmentDbtablesViewBinding
import com.kraftwerk28.pocketcms.viewmodels.DBTablesViewModel
import kotlinx.android.synthetic.main.fragment_dbtables_view.view.*
import kotlinx.android.synthetic.main.item_table.view.*

class DBTablesViewFragment : Fragment() {

    private lateinit var adapter: DBTablesAdapter
    lateinit var viewModel: DBTablesViewModel
    lateinit var binding: FragmentDbtablesViewBinding
    private val itemTouchHelperCallback = ItemSwipeHelper {
        val tableName = it.itemView.tableNameText.toString()
        val pos = viewModel.tables.value?.indexOf(tableName)
        if (pos == null) return@ItemSwipeHelper
        ConfirmAction(requireContext(), "Remove table $tableName?", {
            Log.i(javaClass.simpleName, "Removed table")
        }, {
            adapter.notifyItemChanged(pos)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dbtables_view,
            container,
            false
        )
        binding.setLifecycleOwner(this)
        viewModel = DBTablesViewModel()
        binding.viewModel = viewModel
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

        val inflated = inflater.inflate(
            R.layout.fragment_dbtables_view,
            container,
            false
        )

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
                    swipeRefreshLayout.isRefreshing = it.isEmpty()
                }
            )

            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        viewModel.fetchTables()

        return inflated
    }
}
