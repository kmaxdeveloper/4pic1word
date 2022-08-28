package uz.kmax.a4pic1word.fragment

import android.widget.Toast
import uz.kmax.a4pic1word.databinding.LayoutMenuBinding
import uz.kmax.a4pic1word.gamelevel.GameLevel
import uz.kmax.a4pic1word.utils.SharedHelper

class MenuFragment : BaseFragment<LayoutMenuBinding>(LayoutMenuBinding::inflate) {
    private val shared by lazy { SharedHelper(requireContext()) }
    private val list = GameLevel()
    override fun onViewCreated() {
        val lastLevel = shared.getLastLevelCount()
        val lastLevelCount = lastLevel + 1
        binding.img1.setImageResource(list.image1[lastLevel])
        binding.img2.setImageResource(list.image2[lastLevel])
        binding.img3.setImageResource(list.image3[lastLevel])
        binding.img4.setImageResource(list.image4[lastLevel])

        binding.img1.clipToOutline = true
        binding.img2.clipToOutline = true
        binding.img3.clipToOutline = true
        binding.img4.clipToOutline = true

        binding.level.text = "Level ${lastLevelCount.toString()}"

        binding.play.setOnClickListener {
            replaceFragment(GameFragment())
        }
        binding.setting.setOnClickListener {
            replaceFragment(SettingFragment())
        }
        binding.market.setOnClickListener {
            replaceFragment(MarketFragment())
        }
        binding.account.setOnClickListener {
            replaceFragment(AccountFragment())
        }
    }
}