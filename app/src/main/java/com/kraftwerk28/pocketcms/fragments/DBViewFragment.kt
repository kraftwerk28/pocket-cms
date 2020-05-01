package com.kraftwerk28.pocketcms.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.kraftwerk28.pocketcms.R
import com.kraftwerk28.pocketcms.adapters.DBListAdapter
import kotlinx.android.synthetic.main.fragment_dbview.*
import kotlinx.android.synthetic.main.fragment_dbview.view.*
import org.postgresql.Driver
import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager
import kotlin.concurrent.thread

class DBViewFragment : Fragment() {

    private lateinit var conn: Connection
    private lateinit var adapter: DBListAdapter
    private lateinit var inflated: View
    // TODO: move to ViewModel
    val dbList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        inflated = inflater.inflate(R.layout.fragment_dbview, container, false)

        activity?.actionBar?.title = "Databases"
        adapter = DBListAdapter(this)
        inflated.run {
            dbListView.adapter = adapter
            refreshLayout.setOnRefreshListener {
                fetchDbList()
            }
        }

        fetchDbList()

        return inflated
    }

    fun fetchDbList() {
        setListLoading()
        thread {
            try {
                val registeredDriver = Driver()
                DriverManager.registerDriver(registeredDriver)
                conn = DriverManager.getConnection(
                    "jdbc:postgresql://192.168.0.105:5432/kraftwerk28",
//                    "jdbc:postgresql://10.0.2.2:5432/kraftwerk28",
                    "kraftwerk28",
                    "271828"
                )
                conn.createStatement().use {
                    dbList.clear()
                    it.executeQuery("SELECT * FROM pg_database").use {
                        while (it.next()) {
                            dbList.add(it.getString("datname"))
                        }
                        activity?.runOnUiThread {
                            setListLoading(false)
                            Log.i("DB", dbList.toString())
                            adapter.notifyDataSetChanged()
                        }
                    }
                }

            } catch (e: Exception) {
                activity?.runOnUiThread {
                    Toast
                        .makeText(this@DBViewFragment.activity, e.message, Toast.LENGTH_SHORT)
                        .show()
                }
                Log.w("DB", e.toString())
            }
        }
    }

    fun setListLoading(loading: Boolean = true) {
        inflated.refreshLayout.isRefreshing = loading
    }

}
