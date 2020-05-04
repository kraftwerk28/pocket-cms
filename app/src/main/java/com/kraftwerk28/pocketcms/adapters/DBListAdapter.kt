package com.kraftwerk28.pocketcms.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.kraftwerk28.pocketcms.R
import kotlinx.android.synthetic.main.dbview_item.view.*

class DBListAdapter(val context: Fragment) :
    RecyclerView.Adapter<DBListAdapter.ViewHolder>() {

    var dbList: List<String> = mutableListOf()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dbName = dbList[position]
        holder.itemView.dbnameText.text = dbName
        holder.itemView.setOnClickListener {
            Toast.makeText(context.activity, dbName, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.dbview_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = dbList.size
}
