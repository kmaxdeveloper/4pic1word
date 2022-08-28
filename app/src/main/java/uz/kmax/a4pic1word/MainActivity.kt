package uz.kmax.a4pic1word

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.kmax.a4pic1word.controller.FragmentController
import uz.kmax.a4pic1word.fragment.SplashFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FragmentController.init(R.id.container, supportFragmentManager)
        FragmentController.controller?.startMainFragment(SplashFragment())
    }
}