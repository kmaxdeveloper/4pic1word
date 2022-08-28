package uz.kmax.a4pic1word.utils

import android.view.View

fun View.gone() { this.visibility = View.GONE }

fun View.visible() { this.visibility = View.VISIBLE }

fun View.invisible() { this.visibility = View.INVISIBLE }

fun View.isInvisible() = this.visibility == View.INVISIBLE