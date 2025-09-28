package com.roomrental.android.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object FallbackDialog {

    fun showSuccess(
        context: Context,
        title: String = "Success",
        message: String,
        buttonText: String = "OK",
        callback: (() -> Unit)? = null
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle("✅ $title")
            .setMessage(message)
            .setPositiveButton(buttonText) { _, _ ->
                callback?.invoke()
            }
            .setCancelable(true)
            .show()
    }

    fun showError(
        context: Context,
        title: String = "Error",
        message: String,
        buttonText: String = "OK",
        callback: (() -> Unit)? = null
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle("❌ $title")
            .setMessage(message)
            .setPositiveButton(buttonText) { _, _ ->
                callback?.invoke()
            }
            .setCancelable(true)
            .show()
    }

    fun showWarning(
        context: Context,
        title: String = "Warning",
        message: String,
        buttonText: String = "OK",
        callback: (() -> Unit)? = null
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle("⚠️ $title")
            .setMessage(message)
            .setPositiveButton(buttonText) { _, _ ->
                callback?.invoke()
            }
            .setCancelable(true)
            .show()
    }

    fun showInfo(
        context: Context,
        title: String = "Information",
        message: String,
        buttonText: String = "OK",
        callback: (() -> Unit)? = null
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle("ℹ️ $title")
            .setMessage(message)
            .setPositiveButton(buttonText) { _, _ ->
                callback?.invoke()
            }
            .setCancelable(true)
            .show()
    }

    fun showConfirmation(
        context: Context,
        title: String = "Confirm",
        message: String,
        positiveText: String = "Yes",
        negativeText: String = "No",
        positiveCallback: (() -> Unit)? = null,
        negativeCallback: (() -> Unit)? = null
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle("⚠️ $title")
            .setMessage(message)
            .setPositiveButton(positiveText) { _, _ ->
                positiveCallback?.invoke()
            }
            .setNegativeButton(negativeText) { _, _ ->
                negativeCallback?.invoke()
            }
            .setCancelable(true)
            .show()
    }
}