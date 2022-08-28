package uz.kmax.a4pic1word.ui

import android.R
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import uz.kmax.a4pic1word.databinding.WinDialogBinding

class LastDialog() {
    private var okClickListener : (()-> Unit)? = null
    fun setOkListener(f: ()-> Unit){ okClickListener = f }
    fun show(context: Context){
        val dialog = Dialog(context, R.style.Theme_Black_NoTitleBar_Fullscreen)
        val lastDialogBinding = WinDialogBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(lastDialogBinding.root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        lastDialogBinding.endDialogBtn.setOnClickListener {
            okClickListener?.invoke()
            dialog.dismiss()
        }
        dialog.show()
    }
}