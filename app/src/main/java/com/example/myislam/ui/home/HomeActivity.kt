package com.example.myislam.ui.home

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.example.myislam.R
import com.example.myislam.databinding.ActivityHomeBinding
import com.example.myislam.ui.home.fragments.hadeth.HadethFragment
import com.example.myislam.ui.home.fragments.quran.QuranFragment
import com.example.myislam.ui.home.fragments.radio.RadioFragment
import com.example.myislam.ui.home.fragments.suna.SunaFragment
import com.example.myislam.ui.home.fragments.tasbeh.TasbehFragment
import dagger.hilt.android.AndroidEntryPoint


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private var _binding: ActivityHomeBinding? = null
    private val binding: ActivityHomeBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adjustLayoutForSystemBars()

        defineBottomNavSelectListener()
        binding.bottomNav.selectedItemId = R.id.navigation_quran
    }

    private fun adjustLayoutForSystemBars() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val windowInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(
                windowInsets.left,
                windowInsets.top,
                windowInsets.right,
                windowInsets.bottom
            )
            WindowInsetsCompat.CONSUMED
        }

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        windowInsetsController.isAppearanceLightNavigationBars = false
        windowInsetsController.isAppearanceLightStatusBars = false
    }

    private fun defineBottomNavSelectListener() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            val selectedFragment = when (item.itemId) {
                R.id.navigation_quran -> QuranFragment()
                R.id.navigation_hadeth -> HadethFragment()
                R.id.navigation_tasbeh -> TasbehFragment()
                R.id.navigation_radio -> RadioFragment()
                else -> SunaFragment() // R.id.navigation_suna
            }

            showFragment(selectedFragment)
            true
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                androidx.appcompat.R.anim.abc_fade_in,
                androidx.appcompat.R.anim.abc_fade_out
            )
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}