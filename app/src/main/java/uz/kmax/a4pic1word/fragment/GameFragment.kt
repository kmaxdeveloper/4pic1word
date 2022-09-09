package uz.kmax.a4pic1word.fragment

import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
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
    private val letterListArray = ArrayList<Int>()
    private val wordListArray = ArrayList<String>()
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
    var adType = 0
    private var mInterstitialAd: InterstitialAd? = null

    override fun onViewCreated() {
        // Google Ads
        MobileAds.initialize(requireContext()) {}
        //

        lastCoinCount = shared.getLastCoinCount()
        lastLevelCount = shared.getLastLevelCount()
        binding.level.text = "Level  ${lastLevelCount + 1}"
        binding.coins.text = lastCoinCount.toString()
        getAllQuestions()
        gameManager = GameManager(questionsList, lastLevelCount)
        loadViews()

        if (shared.getResume()) {
            letterListArray.addAll(shared.getLetterVisibility())
            wordListArray.addAll(shared.getAllLetters(gameManager.getWordSize()))
            resumeLoadDataToView()
        } else {
            loadDataToView()
        }

        binding.btnBack.setOnClickListener {
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(requireActivity())
                adType = 3
            } else {
                replaceFragment(MenuFragment())
            }
        }

        binding.btnClean.setOnClickListener {
            if (clean) {
                Toast.makeText(requireContext(), "$clean", Toast.LENGTH_SHORT).show()
                for (i in 0 until wordList.size) {
                    if (wordList[i].isClickable) {
                        wordList[i].text = ""
                    }
                }
                for (i in 0 until lettersList.size) {
                    if (lettersList[i].isClickable && lettersList[i].isInvisible) {
                        lettersList[i].visible()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "$clean", Toast.LENGTH_SHORT).show()
                for (i in 0 until wordList.size) {
                    wordList[i].text = ""
                }
                for (i in 0 until lettersList.size) {
                    lettersList[i].visible()
                }
                wordCheck = ""
            }
        }

        binding.btnHelp.setOnClickListener {
            helpDialog.show(requireContext())
            helpDialog.setOkListener {
                when (it) {
                    0 -> {
                        help(10)
                    }
                    1 -> {
                        if (mInterstitialAd != null) {
                            mInterstitialAd?.show(requireActivity())
                            adType = 1
                        } else {
                            Toast.makeText(requireContext(), "Ads not loaded !", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    2 -> {
                        help(25)
                    }
                    3 -> {
                        if (mInterstitialAd != null) {
                            mInterstitialAd?.show(requireActivity())
                            adType = 2
                        } else {
                            Toast.makeText(requireContext(), "Ads not loaded !", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun help(currentCoin: Int) {
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
        } else if (lastCoinCount >= currentCoin && currentCoin == 25) {
            wordCheck = gameManager.getWord()
            check = false
            Thread.sleep(2000)
            lastCoinCount -= 25
            binding.coins.text = lastCoinCount.toString()
            gameCheck()
        } else {
            Toast.makeText(requireContext(), "Tangalar soni kam !!!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStop() {
        val wordsList = ArrayList<String>()
        val letterVisibility = ArrayList<Int>()
        shared.setLastLevelCount(lastLevelCount)
        shared.setLastCoinCount(lastCoinCount)

        for (i in 0 until wordList.size) {
            wordsList.add(wordList[i].text.toString())
        }
        shared.setAllLetters(wordsList)

        for (i in 0 until lettersList.size) {
            if (lettersList[i].isInvisible) {
                letterVisibility.add(-1)
            }else{
                letterVisibility.add(1)
            }
        }

        shared.setLetterVisibility(letterVisibility)
        shared.setResume(true)
        super.onStop()
    }

    private fun getAllQuestions() {
        questionsList = ArrayList()
        for (i in 0 until dataArray.image1.size) {
            questionsList.add(
                QuestionData(
                    arrayListOf(
                        dataArray.image1[i],
                        dataArray.image2[i],
                        dataArray.image3[i],
                        dataArray.image4[i]
                    ),
                    getString(dataArray.word[i]), getString(dataArray.letter[i])
                )
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
        for (i in 0 until gameManager.getWordSize()) {
            if (wordList[i].text.isNotEmpty()) {
                sum += 1
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

    private fun resumeLoadDataToView() {
        for (i in 0 until imagesList.size) {
            imagesList[i].setImageResource(gameManager.getQuestions()[i])
        }

        for (i in 0 until wordList.size) {
            if (gameManager.getWordSize() > i) {
                wordList[i].visible()
                if (shared.getResume()) {
                    wordList[i].text = wordListArray[i]
                } else {
                    wordList[i].text = ""
                }
            } else {
                wordList[i].gone()
            }
        }

        for (i in 0 until lettersList.size) {
            if (letterListArray[i] == -1){
                lettersList[i].invisible()
                lettersList[i].text = gameManager.getLetters()[i].toString()
            }else{
                lettersList[i].visible()
                lettersList[i].text = gameManager.getLetters()[i].toString()
            }
        }
    }

    private fun gameCheck() {
        if (checkWord()) {
            if (gameManager.hasNextQuestion()) {
                nextLevelDialog.show(requireContext(), lastLevelCount)
                nextLevelDialog.setNextListener {
                    if (mInterstitialAd != null) {
                        mInterstitialAd?.show(requireActivity())
                        adType = 4
                    } else {
                        wordCheck = ""
                        lastCoinCount += 10
                        binding.coins.text = lastCoinCount.toString()
                        gameManager.level += 1
                        lastLevelCount+=1
                        binding.level.text = lastLevelCount.toString()
                        loadDataToView()
                        loadViews()
                    }
                }
            } else {
                nextLevelDialog.show(requireContext(), lastLevelCount)
                nextLevelDialog.setNextListener {
                    lastDialog.show(requireContext())
                    lastDialog.setOkListener { replaceFragment(MenuFragment()) }
                }
            }
        }
    }

    private fun checkWord(): Boolean {
        if (check) {
            wordCheck = ""
            for (i in 0 until gameManager.getWordSize()) {
                wordCheck += wordList[i].text.trim()
            }
        }
        check = true
        return gameManager.check(wordCheck)
    }

    private fun ad() {
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
            }

            override fun onAdDismissedFullScreenContent() {
                when (adType) {
                    1 -> {
                        lastCoinCount += 10
                        binding.coins.text = lastCoinCount.toString()
                    }
                    2 -> {
                        wordCheck = gameManager.getWord()
                        check = false
                        gameCheck()
                    }
                    3 -> {
                        replaceFragment(MenuFragment())
                    }
                    4 -> {
                        wordCheck = ""
                        lastCoinCount += 10
                        binding.coins.text = lastCoinCount.toString()
                        gameManager.level += 1
                        binding.level.text = (++lastLevelCount).toString()
                        loadDataToView()
                        loadViews()
                    }
                }
                mInterstitialAd = null
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                // Called when ad fails to show.
                mInterstitialAd = null
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                //Log.d(TAG, "Ad showed fullscreen content.")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            requireContext(),
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Toast.makeText(requireContext(), "$adError", Toast.LENGTH_SHORT).show()
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Toast.makeText(requireContext(), "Ad loaded !", Toast.LENGTH_SHORT).show()
                    mInterstitialAd = interstitialAd
                    ad()
                }
            })
        //activity?.window?.statusBarColor = Color.BLUE
    }
}