package com.kraftwerk28.pocketcms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_dbview.*
import kotlinx.android.synthetic.main.dbview_item.view.*
import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager
import kotlin.concurrent.thread


class DBViewActivity : AppCompatActivity() {

//    private lateinit var conn: Connection
//    private lateinit var adapter: RVAdapter
//    val dbList = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dbview)
//        supportActionBar?.title = "Databases"
//
//        adapter = RVAdapter(this)
//        dbListView.adapter = adapter
//
//        fetchDbList()
//
//        refreshLayout.setOnRefreshListener {
//            fetchDbList()
//        }
    }


}
