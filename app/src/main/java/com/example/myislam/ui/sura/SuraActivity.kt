package com.example.myislam.ui.sura

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.example.myislam.data.models.QuranSura
import com.example.myislam.databinding.ActivitySuraBinding
import com.example.myislam.utils.Constants

class SuraActivity : AppCompatActivity() {
    private var _binding: ActivitySuraBinding? = null
    private val binding: ActivitySuraBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivitySuraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adjustLayoutForSystemBars()

        bindSura(getSura())
        bindVerses(getSuraAyat())
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

    private fun bindSura(sura: QuranSura?) {
        sura?.let {
            binding.content.sura = sura
        }
    }

    private fun getSuraAyat(): List<String> {
        val suraPosition = intent.getIntExtra(Constants.SURA_POSITION, 0)
        val fileContent =
            assets.open("${suraPosition + 1}.txt").bufferedReader().use { it.readText() }
        return fileContent.trim().split("\n")
    }

    private fun bindVerses(verses: List<String>) {
        val adapter = VersesAdapter(verses)
        binding.content.versesRecyclerView.adapter = adapter
    }

    private fun getSura(): QuranSura? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Constants.SURA, QuranSura::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(Constants.SURA)
        }
    }
}