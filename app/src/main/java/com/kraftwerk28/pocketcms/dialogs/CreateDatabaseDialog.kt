package com.kraftwerk28.pocketcms.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.kraftwerk28.pocketcms.R

class CreateDatabaseDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val view = layoutInflater.inflate(R.layout.dialog_create_database, null)
        builder
            .setView(view)
            .setTitle("Create database")
            .setPositiveButton(
                "Create",
                DialogInterface.OnClickListener { dialog, which -> })
            .setNegativeButton("Cancel", null)
        return builder.create()
    }
}
