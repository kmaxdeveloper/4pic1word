package uz.kmax.a4pic1word.fragment

import uz.kmax.a4pic1word.databinding.LayoutMarketBinding

class SimpleMarket : BaseFragment<LayoutMarketBinding>(LayoutMarketBinding::inflate) {
    override fun onViewCreated() {
        binding.back.setOnClickListener {
            backFragment()
        }
    }
}