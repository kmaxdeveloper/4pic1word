package uz.kmax.a4pic1word.fragment

import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import uz.kmax.a4pic1word.databinding.LayoutGameBinding
import uz.kmax.a4pic1word.gamelevel.GameLevel
import uz.kmax.a4pic1word.manager.GameManager
import uz.kmax.a4pic1word.model.QuestionData
import uz.kmax.a4pic1word.ui.HelpDialog
import uz.kmax.a4pic1word.ui.LastDialog
import uz.kmax.a4pic1word.ui.NextLevelDialog
import uz.kmax.a4pic1word.utils.*

class GameFragment : BaseFragment<LayoutGameBinding>(LayoutGameBinding::inflate) {
    private lateinit var questionsList: ArrayList<QuestionData>
    private lateinit var imagesList: ArrayList<AppCompatImageView>
    private lateinit var wordList: ArrayList<AppCompatButton>
    private lateinit var lettersList: ArrayList<AppCompatButton>
    private lateinit var gameManager: GameManager
    private val dataArray = GameLevel()
    private val shared by lazy { SharedHelper(requireContext()) }
    private var wordCheck: String = ""
    private var lastCoinCount: Int = 0
    private var lastLevelCount: Int = 0
    private val lastDialog = LastDialog()
    private val nextLevelDialog = NextLevelDialog()
    private val helpDialog = HelpDialog()
    private var check = true
    var clean = false

    override fun onViewCreated() {
        //lastCoinCount = shared.getLastCoinCount()
        lastCoinCount = 5000
        lastLevelCount = shared.getLastLevelCount()
        binding.btnBack.setOnClickListener { replaceFragment(MenuFragment()) }
        binding.level.text = "Level  ${lastLevelCount+1}"
        binding.coins.text = lastCoinCount.toString()
        getAllQuestions()
        gameManager = GameManager(questionsList, lastLevelCount)
        loadViews()
        loadDataToView()

        binding.btnClean.setOnClickListener {
            if (clean){
                Toast.makeText(requireContext(), "$clean", Toast.LENGTH_SHORT).show()
                for (i in 0 until wordList.size){
                    if (wordList[i].isClickable){
                        wordList[i].text = ""
                    }
                }
                for (i in 0 until lettersList.size){
                    if (lettersList[i].isClickable && lettersList[i].isInvisible){
                        lettersList[i].visible()
                    }
                }
            }else{
                Toast.makeText(requireContext(), "$clean", Toast.LENGTH_SHORT).show()
                for (i in 0 until wordList.size) { wordList[i].text = "" }
                for (i in 0 until lettersList.size) { lettersList[i].visible() }
                wordCheck = ""
            }
        }

        binding.btnHelp.setOnClickListener {
            helpDialog.show(requireContext())
            helpDialog.setOkListener {
                when(it){
                    0->{
                        help(10)
                    }
                    1->{
                        Toast.makeText(requireContext(), "Ads not loaded !", Toast.LENGTH_SHORT).show()
                    }
                    2->{
                        help(25)
                    }
                    3->{
                        Toast.makeText(requireContext(), "Ads not loaded !", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun help(currentCoin:Int){
        if (lastCoinCount >= currentCoin && currentCoin == 10) {
            var n = 0
            for (i in 0 until wordList.size) {
                n = i
                if (wordList[i].text.isEmpty()) {
                    wordList[n].text = gameManager.getWord()[n].toString()
                    clean = true
                    wordList[n].isClickable = false
                    break
                }
            }
            for (i in 0 until lettersList.size) {
                if (lettersList[i].text.toString() == wordList[n].text.toString()) {
                    lettersList[i].invisible()
                    lettersList[i].isClickable = false
                    break
                }
            }
            lastCoinCount -= 10
            binding.coins.text = lastCoinCount.toString()
            gameCheck()
        } else if (lastCoinCount >= currentCoin && currentCoin == 25){
            wordCheck = gameManager.getWord()
            check = false
            Thread.sleep(2000)
            lastCoinCount -= 25
            binding.coins.text = lastCoinCount.toString()
            gameCheck()
        } else{
            Toast.makeText(requireContext(), "Tangalar soni kam !!!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStop() {
        shared.setLastLevelCount(lastLevelCount)
        shared.setLastCoinCount(lastCoinCount)
        super.onStop()
    }

    private fun getAllQuestions() {
        questionsList = ArrayList()
        for (i in 0 until dataArray.image1.size){
            questionsList.add(
                QuestionData(
                    arrayListOf(dataArray.image1[i], dataArray.image2[i], dataArray.image3[i], dataArray.image4[i]),
                    getString(dataArray.word[i]), getString(dataArray.letter[i]))
            )
        }
    }

    private fun loadViews() {
        imagesList = ArrayList()
        wordList = ArrayList()
        lettersList = ArrayList()

        for (i in 0 until binding.imagesLayout.childCount) {
            imagesList.add(binding.imagesLayout.getChildAt(i) as AppCompatImageView)
        }

        for (i in 0 until binding.wordLayout.childCount) {
            wordList.add(binding.wordLayout.getChildAt(i) as AppCompatButton)
            wordList[i].setOnClickListener {
                wordBtnClick(it as AppCompatButton)
            }
        }

        for (i in 0 until binding.letterLayout.childCount) {
            lettersList.add(binding.letterLayout.getChildAt(i) as AppCompatButton)
            lettersList[i].setOnClickListener {
                letterBtnClick(it as AppCompatButton)
            }
        }
    }

    private fun letterBtnClick(button: AppCompatButton) {
        var sum = 0
        for (i in 0 until gameManager.getWordSize()){
            if (wordList[i].text.isNotEmpty()){
                sum+=1
            }
        }

        if (sum != gameManager.getWordSize() || button.isVisible && wordList[gameManager.getWordSize() - 1].text.isEmpty()) {
            button.invisible()
            val word = button.text.toString()
            for (i in 0 until wordList.size) {
                if (wordList[i].text.isEmpty()) {
                    wordList[i].text = word
                    break
                }
            }
            wordCheck = ""
            for (i in 0 until wordList.size) {
                if (wordList[i].text.isNotEmpty()) {
                    wordCheck += wordList[i].text.toString()
                } else {
                    wordCheck = ""
                }
            }
        }
        gameCheck()
    }

    private fun wordBtnClick(it: AppCompatButton) {
        if (it.text.isNotEmpty()) {
            val word = it.text.toString()
            it.text = ""
            for (i in 0 until lettersList.size) {
                if (lettersList[i].isInvisible()
                    && lettersList[i].text.toString().lowercase() == word.lowercase()
                ) {
                    lettersList[i].visible()
                    wordCheck += word
                    print(wordCheck)
                    break
                }
            }
            for (i in 0 until wordList.size) {
                if (wordList[i].text.isNotEmpty()) {
                    wordCheck += wordList[i].toString()
                } else wordCheck = ""
            }
        }
        wordCheck = ""
    }
    private fun loadDataToView() {
        for (i in 0 until imagesList.size) {
            imagesList[i].setImageResource(gameManager.getQuestions()[i])
        }

        for (i in 0 until wordList.size) {
            if (gameManager.getWordSize() > i) {
                wordList[i].visible()
                wordList[i].text = ""
            } else {
                wordList[i].gone()
            }
        }

        for (i in 0 until lettersList.size) {
            lettersList[i].visible()
            lettersList[i].text = gameManager.getLetters()[i].toString()
        }
    }

    private fun gameCheck() {
        if (checkWord()) {
            if (gameManager.hasNextQuestion()) {
                nextLevelDialog.show(requireContext(),lastLevelCount)
                nextLevelDialog.setNextListener {
                    wordCheck = ""
                    lastCoinCount += 10
                    binding.coins.text = lastCoinCount.toString()
                    gameManager.level+=1
                    binding.level.text = (++lastLevelCount).toString()
                    loadDataToView()
                    loadViews()
                }
            }else{
                nextLevelDialog.show(requireContext(),lastLevelCount)
                nextLevelDialog.setNextListener {
                    lastDialog.show(requireContext())
                    lastDialog.setOkListener { replaceFragment(MenuFragment()) }
                }
            }
        }
    }

    private fun checkWord(): Boolean {
        if (check){
            wordCheck = ""
            for (i in 0 until gameManager.getWordSize()) {
                wordCheck += wordList[i].text.trim()
            }
        }
        check = true
        return gameManager.check(wordCheck)
    }
}