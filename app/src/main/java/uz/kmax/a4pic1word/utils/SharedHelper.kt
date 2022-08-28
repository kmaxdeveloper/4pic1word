package uz.kmax.a4pic1word.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Build
import java.util.*

class SharedHelper(var context: Context) {

    private var preferences: SharedPreferences

    private lateinit var editor: SharedPreferences.Editor

    init {
        preferences = context.getSharedPreferences("PICS_GAME", MODE_PRIVATE)
    }

    fun setNightMode(isNightMode: Boolean) {
        editor = preferences.edit()
        editor.putBoolean("IS_NIGHT", isNightMode)
        editor.apply()
    }

    fun getNightMode() = preferences.getBoolean("IS_NIGHT", false)

    fun setLastLevelCount(LastLevel: Int){
        editor = preferences.edit()
        editor.putInt("LAST_LEVEL",LastLevel)
        editor.apply()
    }

    fun getLastLevelCount() = preferences.getInt("LAST_LEVEL",0)

    fun setLastCoinCount(CoinCount: Int){
        editor = preferences.edit()
        editor.putInt("LAST_COIN_COUNT",CoinCount)
        editor.apply()
    }

    fun getLastCoinCount() = preferences.getInt("LAST_COIN_COUNT",0)

    fun setLevelCount(levelCount: Int){
        editor = preferences.edit()
        editor.putInt("LEVEL_COUNT",levelCount)
        editor.apply()
    }

    fun getLevelCount() = preferences.getInt("LEVEL_COUNT",0)

    fun setMusicMode(Mode: Boolean){
        editor = preferences.edit()
        editor.putBoolean("MUSIC_MODE", Mode)
        editor.apply()
    }

    fun getMusicMode() = preferences.getBoolean("MUSIC_MODE",true)

    fun getLanguage() = preferences.getString("LANG", "ru")

    fun loadLocale(context: Context) {
        setLanguage(getLanguage()!!, context)
    }

    fun setLanguage(lang: String, context: Context) {
        editor = preferences.edit()
        editor.putString("LANG", lang)
        editor.apply()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, lang)
        }
        updateResourcesLegacy(context, lang)
    }

    private fun updateResources(context: Context, language: String): Context? {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)//bu joyni uchirib kor
        return context.createConfigurationContext(configuration)
    }


    private fun updateResourcesLegacy(context: Context, language: String): Context? {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        configuration.setLayoutDirection(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }
}