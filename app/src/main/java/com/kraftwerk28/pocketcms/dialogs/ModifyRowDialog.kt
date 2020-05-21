package com.kraftwerk28.pocketcms.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.kraftwerk28.pocketcms.R

class ModifyRowDialog(
    val parent: ViewGroup,
    val header: List<String>,
    val row: Map<String, String>,
    val onSubmit: (r: Map<String, String>) -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater
            .inflate(R.layout.dialog_modify_row, parent, false)
        return AlertDialog.Builder(context)
            .setView(view)
            .setTitle("Modify row")
            .setPositiveButton("Ok") { dialog, which ->
                TODO()
            }
            .setNegativeButton("Cancel") { dialog, which ->
                TODO()
            }
            .create()
    }
}