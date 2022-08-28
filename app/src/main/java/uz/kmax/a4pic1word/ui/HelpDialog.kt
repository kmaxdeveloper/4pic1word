package uz.kmax.a4pic1word.ui

import android.R
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import uz.kmax.a4pic1word.databinding.HelpDialogBinding

class HelpDialog {
    private var okClickListener : ((index:Int)-> Unit)? = null
    fun setOkListener(f: (index : Int)-> Unit){ okClickListener = f }

    fun show(context: Context){
        val dialog = Dialog(context, R.style.Theme_Black_NoTitleBar_Fullscreen)
        val bindingDialog = HelpDialogBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(bindingDialog.root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        bindingDialog.word1.setOnClickListener {
            okClickListener?.invoke(2)
            dialog.dismiss()
        }

        bindingDialog.letter1.setOnClickListener {
            okClickListener?.invoke(0)
            dialog.dismiss()
        }

        bindingDialog.letter1Ads.setOnClickListener {
            okClickListener?.invoke(1)
            dialog.dismiss()
        }

        bindingDialog.dismiss.setOnClickListener {
            dialog.dismiss()
        }

        bindingDialog.word1Ads.setOnClickListener {
            okClickListener?.invoke(3)
            dialog.dismiss()
        }

        dialog.show()
    }
}