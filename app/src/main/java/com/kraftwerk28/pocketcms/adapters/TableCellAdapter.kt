package com.kraftwerk28.pocketcms.adapters

import android.view.ViewGroup
import com.kraftwerk28.pocketcms.databinding.ItemTableRowBinding
import androidx.recyclerview.widget.RecyclerView as RV

class TableCellAdapter() : RV.Adapter<TableCellAdapter.ViewHolder>() {

    class ViewHolder(itemBinding: ItemTableRowBinding) :
        RV.ViewHolder(itemBinding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
