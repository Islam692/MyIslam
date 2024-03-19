package com.example.myislam.home

import android.os.Bundle
import android.view.Menu
import android.view.WindowManager.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myislam.R
import com.example.myislam.databinding.ActivityHomeBinding
import com.example.myislam.hadeth.HadethFragment
import com.example.myislam.quran.QuranFragment
import com.example.myislam.radio.RadioFragment
import com.example.myislam.tasbeh.TasbehFragment
import com.google.android.material.navigation.NavigationBarView


class HomeActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.content.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_quran -> {
                    showTabFragment(QuranFragment())
                }

                R.id.navigation_hadeth -> {
                    showTabFragment(HadethFragment())
                }

                R.id.navigation_tasbeh -> {
                    showTabFragment(TasbehFragment())
                }

                R.id.navigation_radio -> {
                    showTabFragment(RadioFragment())
                }
            }
            true
        }
        viewBinding.content.bottomNav.selectedItemId = R.id.navigation_quran
        viewBinding.content.bottomNav.labelVisibilityMode =
            NavigationBarView.LABEL_VISIBILITY_LABELED
        val window = this.window
        window.addFlags(LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.black)
    }

    private fun showTabFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu items for use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.home_nave_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}