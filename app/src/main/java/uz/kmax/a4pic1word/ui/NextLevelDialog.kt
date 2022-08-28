package uz.kmax.a4pic1word.ui

import android.R
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import uz.kmax.a4pic1word.databinding.NextLevelDialogBinding
import uz.kmax.a4pic1word.gamelevel.GameLevel

class NextLevelDialog {
    private var nextClickListener: (() -> Unit)? = null
    fun setNextListener(f: () -> Unit) {
        nextClickListener = f
    }

    val game = GameLevel()

    fun show(context: Context, gameLevel: Int) {
        val dialog = Dialog(context, R.style.Theme_Black_NoTitleBar_Fullscreen)
        val bindingDialog = NextLevelDialogBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(bindingDialog.root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        bindingDialog.levelImage.setImageResource(game.image1[gameLevel])
        bindingDialog.wordName.text = context.resources.getString(game.word[gameLevel])
        val animImage = bindingDialog.animImage
        val anim: Animation =
            AnimationUtils.loadAnimation(context, uz.kmax.a4pic1word.R.anim.dialog_win_anim)
        animImage.startAnimation(anim)
        bindingDialog.nextLevel.setOnClickListener {
            animImage.clearAnimation()
            nextClickListener?.invoke()
            dialog.dismiss()
        }
        dialog.show()
    }

}