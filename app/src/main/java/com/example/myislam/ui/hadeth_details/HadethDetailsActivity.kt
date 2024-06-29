package com.example.myislam.ui.hadeth_details

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myislam.data.models.Hadeth
import com.example.myislam.databinding.ActivityHadethDetailsBinding
import com.example.myislam.utils.Constants

class HadethDetailsActivity : AppCompatActivity() {
    private var _binding: ActivityHadethDetailsBinding? = null
    private val binding: ActivityHadethDetailsBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityHadethDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindHadeth(getHadeth())
    }

    private fun bindHadeth(hadeth: Hadeth?) {
        hadeth?.let {
            binding.tvTitle.text = hadeth.title
            binding.content.hadethContent.text = hadeth.content
        }
    }

    private fun getHadeth(): Hadeth? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Constants.HADETH, Hadeth::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(Constants.HADETH) as Hadeth?
        }
    }
}