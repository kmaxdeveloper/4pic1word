package uz.kmax.a4pic1word.fragment

import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import uz.kmax.a4pic1word.databinding.LayoutSplashBinding

class SplashFragment : BaseFragment<LayoutSplashBinding>(LayoutSplashBinding::inflate) {
    override fun onViewCreated() {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        object : CountDownTimer(5000, 100) {
            override fun onFinish() {
                replaceFragment(MenuFragment())
            }
            override fun onTick(value: Long) {

            }
        }.start()
    }
}