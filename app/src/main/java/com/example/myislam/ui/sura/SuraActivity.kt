package com.example.myislam.ui.sura

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myislam.data.models.QuranSura
import com.example.myislam.databinding.ActivitySuraBinding
import com.example.myislam.utils.Constants

class SuraActivity : AppCompatActivity() {
    private var _binding: ActivitySuraBinding? = null
    private val binding: ActivitySuraBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySuraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindSura(getSura())
        bindVerses(getSuraAyat())
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