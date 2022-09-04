package uz.kmax.a4pic1word.model

import java.util.*

/**
 *  This Class is Model of Game
 *  Bu Klass O'yinning modeli hisoblanadi
 * */

data class QuestionData(
    var imageList: ArrayList<Int>,
    var word: String,
    var letters: String
)