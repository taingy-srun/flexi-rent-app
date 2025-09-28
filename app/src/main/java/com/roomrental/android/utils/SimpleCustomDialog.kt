package com.roomrental.android.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.roomrental.android.R

object SimpleCustomDialog {

    enum class DialogType(val iconRes: Int) {
        SUCCESS(R.drawable.ic_success),
        ERROR(R.drawable.ic_error),
        WARNING(R.drawable.ic_warning),
        INFO(R.drawable.ic_info)
    }

    fun showSuccess(
        context: Context,
        title: String = "Success",
        message: String,
        buttonText: String = "OK",
        callback: (() -> Unit)? = null
    ): Dialog {
        return showDialog(context, title, message, DialogType.SUCCESS, buttonText, callback)
    }

    fun showError(
        context: Context,
        title: String = "Error",
        message: String,
        buttonText: String = "OK",
        callback: (() -> Unit)? = null
    ): Dialog {
        return showDialog(context, title, message, DialogType.ERROR, buttonText, callback)
    }

    fun showWarning(
        context: Context,
        title: String = "Warning",
        message: String,
        buttonText: String = "OK",
        callback: (() -> Unit)? = null
    ): Dialog {
        return showDialog(context, title, message, DialogType.WARNING, buttonText, callback)
    }

    fun showInfo(
        context: Context,
        title: String = "Information",
        message: String,
        buttonText: String = "OK",
        callback: (() -> Unit)? = null
    ): Dialog {
        return showDialog(context, title, message, DialogType.INFO, buttonText, callback)
    }

    fun showConfirmation(
        context: Context,
        title: String = "Confirm",
        message: String,
        positiveText: String = "Yes",
        negativeText: String = "No",
        positiveCallback: (() -> Unit)? = null,
        negativeCallback: (() -> Unit)? = null
    ): Dialog {
        return showDialogWithTwoButtons(
            context, title, message, DialogType.WARNING,
            positiveText, negativeText, positiveCallback, negativeCallback
        )
    }

    private fun showDialog(
        context: Context,
        title: String,
        message: String,
        type: DialogType,
        buttonText: String,
        callback: (() -> Unit)?
    ): Dialog {
        try {
            println("SimpleCustomDialog: Creating dialog...")

            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

            val view = LayoutInflater.from(context).inflate(R.layout.dialog_custom_message, null)
            dialog.setContentView(view)

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(true)

            // Find views
            val ivIcon = view.findViewById<ImageView>(R.id.ivIcon)
            val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
            val tvMessage = view.findViewById<TextView>(R.id.tvMessage)
            val btnConfirm = view.findViewById<Button>(R.id.btnConfirm)
            val btnCancel = view.findViewById<Button>(R.id.btnCancel)

            // Set content
            ivIcon.setImageResource(type.iconRes)
            tvTitle.text = title
            tvMessage.text = message
            btnConfirm.text = buttonText

            // Hide cancel button for single-button dialogs
            btnCancel.visibility = View.GONE

            // Set click listener
            btnConfirm.setOnClickListener {
                callback?.invoke()
                dialog.dismiss()
            }

            println("SimpleCustomDialog: Showing dialog...")
            dialog.show()
            println("SimpleCustomDialog: Dialog shown successfully!")

            return dialog
        } catch (e: Exception) {
            println("SimpleCustomDialog Error: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    private fun showDialogWithTwoButtons(
        context: Context,
        title: String,
        message: String,
        type: DialogType,
        positiveText: String,
        negativeText: String,
        positiveCallback: (() -> Unit)?,
        negativeCallback: (() -> Unit)?
    ): Dialog {
        try {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

            val view = LayoutInflater.from(context).inflate(R.layout.dialog_custom_message, null)
            dialog.setContentView(view)

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(true)

            // Find views
            val ivIcon = view.findViewById<ImageView>(R.id.ivIcon)
            val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
            val tvMessage = view.findViewById<TextView>(R.id.tvMessage)
            val btnConfirm = view.findViewById<Button>(R.id.btnConfirm)
            val btnCancel = view.findViewById<Button>(R.id.btnCancel)

            // Set content
            ivIcon.setImageResource(type.iconRes)
            tvTitle.text = title
            tvMessage.text = message
            btnConfirm.text = positiveText
            btnCancel.text = negativeText

            // Show both buttons
            btnCancel.visibility = View.VISIBLE

            // Set click listeners
            btnConfirm.setOnClickListener {
                positiveCallback?.invoke()
                dialog.dismiss()
            }

            btnCancel.setOnClickListener {
                negativeCallback?.invoke()
                dialog.dismiss()
            }

            dialog.show()
            return dialog
        } catch (e: Exception) {
            println("SimpleCustomDialog Error: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
}