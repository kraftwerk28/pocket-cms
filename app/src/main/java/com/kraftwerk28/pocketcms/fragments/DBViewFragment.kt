package com.kraftwerk28.pocketcms.fragments

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.kraftwerk28.pocketcms.Database
import com.kraftwerk28.pocketcms.R
import com.kraftwerk28.pocketcms.adapters.DBListAdapter
import com.kraftwerk28.pocketcms.viewmodels.DBViewModel
import kotlinx.android.synthetic.main.fragment_dbview.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO_PARALLELISM_PROPERTY_NAME
import kotlinx.coroutines.launch

class DBViewFragment : Fragment() {

    private lateinit var adapter: DBListAdapter
    private lateinit var inflated: View
    private lateinit var viewModel: DBViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreateView(inflater, container, savedInstanceState)
        inflated = inflater.inflate(R.layout.fragment_dbview, container, false)

        viewModel = DBViewModel()

        activity?.actionBar?.title = "Databases"
        adapter = DBListAdapter(this)
        val creds = arguments?.getSerializable("creds") as Database.Credentials
        inflated.run {
            dbListView.adapter = adapter
            refreshLayout.setOnRefreshListener {
                viewModel.fetchDbList()
            }
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
