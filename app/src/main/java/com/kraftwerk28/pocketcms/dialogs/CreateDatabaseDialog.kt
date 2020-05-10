package com.kraftwerk28.pocketcms.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.kraftwerk28.pocketcms.R
import kotlinx.android.synthetic.main.dialog_create_database.view.*

class CreateDatabaseDialog(
    val parent: ViewGroup,
    val onCreateCallback: (dbName: String) -> Unit
) :
    DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val view = layoutInflater
            .inflate(R.layout.dialog_create_database, parent, false)
        builder
            .setView(view)
            .setTitle("Create database")
            .setPositiveButton("Create")
            { dialog, which ->
                onCreateCallback(view.dbNameInput.text.toString())
            }
            .setNegativeButton("Cancel", null)
        return builder.create()
    }
}
