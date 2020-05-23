package com.kraftwerk28.pocketcms

import android.app.AlertDialog
import android.content.Context

class ConfirmAction(
    context: Context,
    title: String,
    okCallback: () -> Unit,
    cancelCallback: () -> Unit
) {
    val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
        .setTitle(title)
        .setPositiveButton("Yes") { _, _ -> okCallback() }
        .setNegativeButton("Cancel") { _, _ -> cancelCallback() }
        .setOnCancelListener { cancelCallback() }

    fun invoke() = dialogBuilder.show()
}