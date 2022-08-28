package uz.kmax.a4pic1word.fragment

import android.view.Menu
import android.widget.Toast
import uz.kmax.a4pic1word.databinding.LayoutMarketBinding
import uz.kmax.a4pic1word.utils.SharedHelper

class MarketFragment:BaseFragment<LayoutMarketBinding>(LayoutMarketBinding::inflate) {
    val shared by lazy { SharedHelper(requireContext()) }
    override fun onViewCreated() {

        val coinCount  = shared.getLastCoinCount()

        binding.coinCount.text = coinCount.toString()

        binding.back.setOnClickListener {
            replaceFragment(MenuFragment())
        }

        binding.miniPackage.setOnClickListener {
            Toast.makeText(requireContext(), "Mini Package", Toast.LENGTH_SHORT).show()
        }

        binding.litePackage.setOnClickListener {
            Toast.makeText(requireContext(), "Lite Package", Toast.LENGTH_SHORT).show()
        }

        binding.standardPackage.setOnClickListener {
            Toast.makeText(requireContext(), "Standard Package", Toast.LENGTH_SHORT).show()
        }

        binding.proPackage.setOnClickListener {
            Toast.makeText(requireContext(), "Pro Package", Toast.LENGTH_SHORT).show()
        }

        binding.masterPackage.setOnClickListener {
            Toast.makeText(requireContext(), "Master Package", Toast.LENGTH_SHORT).show()
        }
    }
}