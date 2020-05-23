package com.kraftwerk28.pocketcms.dialogs

import android.app.Activity
import android.app.AlertDialog
import com.kraftwerk28.pocketcms.R
import kotlinx.android.synthetic.main.dialog_create_database.view.*

class CreateDatabaseDialog(
    activity: Activity,
    onCreateCallback: (dbName: String) -> Unit
) {
    val dialogBuilder: AlertDialog.Builder

    init {
        val view = activity
            .layoutInflater
            .inflate(R.layout.dialog_create_database, null)
        dialogBuilder = AlertDialog.Builder(activity)
            .setView(view)
            .setTitle("Create database")
            .setPositiveButton("Create")
            { dialog, which ->
                onCreateCallback(view.dbNameInput.text.toString())
            }
            .setNegativeButton("Cancel", null)
    }

    fun show() = dialogBuilder.show()
}
