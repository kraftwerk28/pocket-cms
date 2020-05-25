package com.kraftwerk28.pocketcms.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.kraftwerk28.pocketcms.R

class CreateTableDialog(context: Context) {
    val builder: AlertDialog.Builder

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_create_table, null)
        builder = AlertDialog.Builder(context)
//            .setView()
    }

    fun invoke() = builder.show()
}