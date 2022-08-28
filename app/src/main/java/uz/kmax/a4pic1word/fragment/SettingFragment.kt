package uz.kmax.a4pic1word.fragment

import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import uz.kmax.a4pic1word.databinding.LayoutSettingBinding
import uz.kmax.a4pic1word.utils.SharedHelper

class SettingFragment: BaseFragment<LayoutSettingBinding>(LayoutSettingBinding::inflate) {
    val shared by lazy { SharedHelper(requireContext()) }
    override fun onViewCreated() {
        binding.backBtn.setOnClickListener {
            replaceFragment(MenuFragment())
        }

        binding.musicMode.isChecked = shared.getMusicMode()
        binding.musicMode.setOnClickListener{
            shared.setMusicMode(binding.musicMode.isChecked)

            Toast.makeText(requireContext(), "Music Mode Clicked !", Toast.LENGTH_SHORT).show()
        }

        binding.nightMode.isChecked = shared.getNightMode()
        binding.nightMode.setOnClickListener {
            shared.setNightMode(binding.nightMode.isChecked)
            if (binding.nightMode.isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.langEng.setOnClickListener {
            shared.setLanguage("en", requireContext())
            replaceFragment(MenuFragment())
        }

        binding.langUzbek.setOnClickListener {
            shared.setLanguage("uz", requireContext())
            replaceFragment(MenuFragment())
        }
    }
}