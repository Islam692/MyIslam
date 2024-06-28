package com.example.myislam.ui.home.fragments.tasbeh

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myislam.R
import com.example.myislam.databinding.FragmentTasbehBinding

class TasbehFragment : Fragment() {
    lateinit var viewBinding: FragmentTasbehBinding
    var times_clicked = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentTasbehBinding.inflate(layoutInflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var mediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.subhan_allah)
        mediaPlayer.start()
        viewBinding.counter.setOnClickListener {
            startTasbeh()
        }
    }

    private fun startTasbeh() {
        times_clicked++
        viewBinding.counter.text = times_clicked.toString()
        viewBinding.progressBar.incrementProgressBy(3)
        if (times_clicked == 33) {
            viewBinding.tvTasbeh.text = "لله\nالحمد"
            viewBinding.progressBar.progress = 0
            viewBinding.progressBar.incrementProgressBy(0)
            var mediaplayer = MediaPlayer.create(context, R.raw.alhamd_lullah)
            mediaplayer.start()
        } else if (times_clicked == 66) {
            viewBinding.tvTasbeh.text = "الله\nأكبر"
            viewBinding.progressBar.progress = 0
            viewBinding.progressBar.incrementProgressBy(0)
            var mediaplayer = MediaPlayer.create(context, R.raw.allah_akbr)
            mediaplayer.start()
        } else if (times_clicked == 100) {
            times_clicked = 0
            viewBinding.tvTasbeh.text = "الله\nسبحان"
            viewBinding.progressBar.progress = 0
            viewBinding.progressBar.incrementProgressBy(0)
            var mediaplayer = MediaPlayer.create(context, R.raw.la_elah_ala_allah)
            mediaplayer.start()
            Toast.makeText(
                context,
                "التسبيحة المائة \n لا اله الا الله له الملك وله الحمد",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}