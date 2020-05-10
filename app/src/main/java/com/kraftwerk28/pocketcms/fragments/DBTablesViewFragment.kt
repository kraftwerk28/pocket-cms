package com.kraftwerk28.pocketcms.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.kraftwerk28.pocketcms.R
import com.kraftwerk28.pocketcms.adapters.DBTablesAdapter
import com.kraftwerk28.pocketcms.databinding.FragmentDbtablesViewBinding
import com.kraftwerk28.pocketcms.viewmodels.DBTablesViewModel
import kotlinx.android.synthetic.main.fragment_dbtables_view.view.*

class DBTablesViewFragment : Fragment() {

    lateinit var viewModel: DBTablesViewModel
    lateinit var binding: FragmentDbtablesViewBinding

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
        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().navigate(R.id.action_DBTablesViewFragment_to_DBConnectFragment2)
        }

        val inflated = inflater.inflate(
            R.layout.fragment_dbtables_view,
            container,
            false
        )

        inflated.run {
            dbTablesView.adapter = DBTablesAdapter(viewModel)
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.fetchTables()
            }
            viewModel.tables.observe(
                this@DBTablesViewFragment,
                Observer {
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
