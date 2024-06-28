package com.example.myislam.ui.home.fragments.tasbeh

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myislam.R
import com.example.myislam.databinding.FragmentTasbehBinding
import com.example.myislam.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TasbehFragment : Fragment() {
    private var _binding: FragmentTasbehBinding? = null
    private val binding: FragmentTasbehBinding get() = _binding!!

    @Inject
    lateinit var utils: Utils

    private var clickCounter = 0
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasbehBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaPlayer = MediaPlayer()

        initializeViews()

        binding.root.setOnClickListener { countTasbeh() }
    }

    private fun initializeViews() {
        binding.tvTasbeh.text = getString(R.string.tasbeeh_text)
        binding.counter.text = clickCounter.toString()
        binding.progressBar.progress = clickCounter
        playSound(R.raw.subhan_allah)
    }

    private fun countTasbeh() {
        clickCounter++
        binding.counter.text = clickCounter.toString()
        binding.progressBar.incrementProgressBy(1)

        when (clickCounter) {
            33 -> updateZekr(R.string.hamd_text, R.raw.alhamd_lullah)
            66 -> updateZekr(R.string.takbeer_text, R.raw.allah_akbr)

            100 -> {
                clickCounter = 0
                utils.showShortToast(getString(R.string.tahleel_text))
                updateZekr(R.string.tasbeeh_text, R.raw.la_elah_ala_allah)
            }
        }
    }

    private fun playSound(rawResId: Int) {
        mediaPlayer.reset()

        val assetFileDescriptor = resources.openRawResourceFd(rawResId)
        mediaPlayer.setDataSource(
            assetFileDescriptor.fileDescriptor,
            assetFileDescriptor.startOffset,
            assetFileDescriptor.length
        )
        assetFileDescriptor.close()

        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener { it.start() }
    }

    private fun updateZekr(zekrTextResId: Int, zekrAudioResId: Int) {
        binding.tvTasbeh.text = getString(zekrTextResId)
        binding.progressBar.setProgress(0, true)
        playSound(zekrAudioResId)
    }
}
