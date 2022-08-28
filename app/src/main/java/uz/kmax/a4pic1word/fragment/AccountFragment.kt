package uz.kmax.a4pic1word.fragment

import uz.kmax.a4pic1word.databinding.LayoutAccountBinding

class AccountFragment: BaseFragment<LayoutAccountBinding>(LayoutAccountBinding::inflate) {
    override fun onViewCreated() {
        binding.back.setOnClickListener {
            replaceFragment(MenuFragment())
        }
    }
}