package com.roomrental.android.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.annotation.DrawableRes
import com.roomrental.android.R
import com.roomrental.android.databinding.DialogCustomMessageBinding

class CustomDialog(private val context: Context) {

    enum class DialogType(val iconRes: Int) {
        SUCCESS(R.drawable.ic_success),
        ERROR(R.drawable.ic_error),
        WARNING(R.drawable.ic_warning),
        INFO(R.drawable.ic_info)
    }

    class Builder(private val context: Context) {
        private var title: String = "FlexiRent"
        private var message: String = ""
        private var dialogType: DialogType = DialogType.INFO
        private var positiveButtonText: String = "OK"
        private var negativeButtonText: String = "Cancel"
        private var showNegativeButton: Boolean = false
        private var positiveButtonCallback: (() -> Unit)? = null
        private var negativeButtonCallback: (() -> Unit)? = null
        private var cancelable: Boolean = true

        fun setTitle(title: String) = apply { this.title = title }

        fun setMessage(message: String) = apply { this.message = message }

        fun setType(type: DialogType) = apply { this.dialogType = type }

        fun setPositiveButton(text: String, callback: (() -> Unit)? = null) = apply {
            this.positiveButtonText = text
            this.positiveButtonCallback = callback
        }

        fun setNegativeButton(text: String, callback: (() -> Unit)? = null) = apply {
            this.negativeButtonText = text
            this.negativeButtonCallback = callback
            this.showNegativeButton = true
        }

        fun setCancelable(cancelable: Boolean) = apply { this.cancelable = cancelable }

        fun show(): Dialog {
            try {
                println("CustomDialog: Creating dialog...")
                val binding = DialogCustomMessageBinding.inflate(LayoutInflater.from(context))
                val dialog = Dialog(context)

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(binding.root)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.setCancelable(cancelable)

                println("CustomDialog: Dialog created, setting content...")

            // Set content
            binding.tvTitle.text = title
            binding.tvMessage.text = message
            binding.ivIcon.setImageResource(dialogType.iconRes)
            binding.btnConfirm.text = positiveButtonText

            // Handle buttons
            if (showNegativeButton) {
                binding.btnCancel.visibility = View.VISIBLE
                binding.btnCancel.text = negativeButtonText
                binding.btnCancel.setOnClickListener {
                    negativeButtonCallback?.invoke()
                    dialog.dismiss()
                }
            } else {
                binding.btnCancel.visibility = View.GONE
            }

            binding.btnConfirm.setOnClickListener {
                positiveButtonCallback?.invoke()
                dialog.dismiss()
            }

                println("CustomDialog: Showing dialog...")
                dialog.show()
                println("CustomDialog: Dialog shown successfully!")
                return dialog
            } catch (e: Exception) {
                println("CustomDialog Error: ${e.message}")
                e.printStackTrace()
                throw e
            }
        }
    }

    companion object {
        // Convenience methods for common dialog types

        fun showSuccess(
            context: Context,
            title: String = "Success",
            message: String,
            buttonText: String = "OK",
            callback: (() -> Unit)? = null
        ): Dialog {
            return Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setType(DialogType.SUCCESS)
                .setPositiveButton(buttonText, callback)
                .show()
        }

        fun showError(
            context: Context,
            title: String = "Error",
            message: String,
            buttonText: String = "OK",
            callback: (() -> Unit)? = null
        ): Dialog {
            return Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setType(DialogType.ERROR)
                .setPositiveButton(buttonText, callback)
                .show()
        }

        fun showWarning(
            context: Context,
            title: String = "Warning",
            message: String,
            buttonText: String = "OK",
            callback: (() -> Unit)? = null
        ): Dialog {
            return Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setType(DialogType.WARNING)
                .setPositiveButton(buttonText, callback)
                .show()
        }

        fun showInfo(
            context: Context,
            title: String = "Information",
            message: String,
            buttonText: String = "OK",
            callback: (() -> Unit)? = null
        ): Dialog {
            return Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setType(DialogType.INFO)
                .setPositiveButton(buttonText, callback)
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
        ): Dialog {
            return Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setType(DialogType.WARNING)
                .setPositiveButton(positiveText, positiveCallback)
                .setNegativeButton(negativeText, negativeCallback)
                .show()
        }
    }
}