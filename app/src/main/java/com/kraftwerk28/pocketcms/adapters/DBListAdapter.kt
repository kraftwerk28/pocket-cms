package com.kraftwerk28.pocketcms.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.kraftwerk28.pocketcms.R
import com.kraftwerk28.pocketcms.fragments.DBViewFragment
import kotlinx.android.synthetic.main.dbview_item.view.*

class DBListAdapter(val context: DBViewFragment) :
    RecyclerView.Adapter<DBListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dbName = context.dbList[position]
        holder.itemView.dbnameText.text = dbName
        holder.itemView.setOnClickListener {
            Toast.makeText(this@DBListAdapter.context.activity, dbName, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.dbview_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = context.dbList.size
}
