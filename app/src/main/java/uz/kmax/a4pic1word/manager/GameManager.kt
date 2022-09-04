package uz.kmax.a4pic1word.manager

import uz.kmax.a4pic1word.model.QuestionData
import java.util.*
/**
 *  4 pic 1 word game Source Code
 *  4 rasm 1 so'z o'yini ichki kodi
 *  All Code is writed in kotlin language
 *  Barcha kodlar kotlin tilida yozilgan
 *
 *  This Class is Game Manager for controlling game
 *
 *  Bu O'yinni Boshqaruchi klass ...
 * */

class GameManager(
    private var questionsList: ArrayList<QuestionData>,
    var level: Int
) {
    fun getLevelCount() = level
    fun question() = questionsList[level]
    fun getQuestions() = question().imageList
    fun getWord() = question().word
    fun getQuestionSize() = questionsList.size
    fun getWordSize() = question().word.length
    fun getLetters() = question().letters
    fun getLettersSize() = question().letters.length
    fun questionsSize() = questionsList.size

    fun check(word: String): Boolean {
        return getWord().trim().lowercase() == word.lowercase()
    }

    fun hasNextQuestion(): Boolean {
        if (getLevelCount()+1 < getQuestionSize()) { return true }
        return false
    }

}