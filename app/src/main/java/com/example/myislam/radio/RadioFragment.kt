package com.example.myislam.radio

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.myislam.Constants
import com.example.myislam.R
import com.example.myislam.api.Radio
import com.example.myislam.databinding.FragmentRadioBinding


class RadioFragment : Fragment() {
    private lateinit var binding: FragmentRadioBinding
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var radioPlayerService: RadioPlayerService
    private var isRadioPlayerServiceBound: Boolean = false

    private val radioPlayerServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as RadioPlayerService.LocalBinder
            radioPlayerService = binder.getService()
            isRadioPlayerServiceBound = true
            defineRadioPlayerServiceContract()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isRadioPlayerServiceBound = false
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        Intent(requireContext(), RadioPlayerService::class.java).also { intent ->
            requireActivity().startForegroundService(intent)
            requireActivity().bindService(
                intent,
                radioPlayerServiceConnection,
                Context.BIND_AUTO_CREATE
            )
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().unbindService(radioPlayerServiceConnection)
        isRadioPlayerServiceBound = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRadioBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences(
            Constants.SETTINGS_FILE_NAME,
            Context.MODE_PRIVATE
        )

        initRadioService()

        binding.play.setOnClickListener { toggleRadioPlayer() }
        binding.next.setOnClickListener { playNextRadio() }
        binding.previous.setOnClickListener { playPreviousRadio() }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun initRadioService() {
        val serviceIntent = Intent(requireContext(), RadioPlayerService::class.java)
        serviceIntent.putExtra(Constants.START_ACTION, Constants.INIT_SERVICE)
        requireActivity().startForegroundService(serviceIntent)

        togglePlayingVisibility(false)
        togglePlayingStatus(false)
    }

    private fun defineRadioPlayerServiceContract() {
        radioPlayerService.defineRadioMediaPlayerContract(object :
            RadioPlayerService.RadioMediaPlayerContract {
            override fun onPlayed(radio: Radio) {
                togglePlayingVisibility(true)
                togglePlayingStatus(true)
                binding.izaaTv.text = radio.name
            }

            override fun onPaused() {
                togglePlayingVisibility(true)
                togglePlayingStatus(false)
            }

            override fun onNextPlayed(radio: Radio) {
                togglePlayingVisibility(true)
                togglePlayingStatus(true)
                binding.izaaTv.text = radio.name
            }

            override fun onPreviousPlayed(radio: Radio) {
                togglePlayingVisibility(true)
                togglePlayingStatus(true)
                binding.izaaTv.text = radio.name
            }

            override fun onLoading() {
                togglePlayingVisibility(false)
            }
        })
    }

    private fun toggleRadioPlayer() {
        if (isRadioPlayerServiceBound) {
            radioPlayerService.playOrPauseRadio()
        }
    }

    private fun playPreviousRadio() {
        if (isRadioPlayerServiceBound) {
            radioPlayerService.playPreviousRadio()
            togglePlayingVisibility(false)
            togglePlayingStatus(false)
        }
    }

    private fun playNextRadio() {
        if (isRadioPlayerServiceBound) {
            radioPlayerService.playNextRadio()
            togglePlayingVisibility(false)
            togglePlayingStatus(false)
        }
    }

    private fun togglePlayingVisibility(playing: Boolean) {
        binding.play.isVisible = playing
        binding.loadingProgress.isVisible = !playing
    }

    private fun togglePlayingStatus(currentlyPlaying: Boolean) {
        if (currentlyPlaying) {
            binding.play.setImageResource(R.drawable.ic_play_gold)
            binding.play.scaleType = ImageView.ScaleType.CENTER
        } else {
            binding.play.setImageResource(R.drawable.ic_pause)
            binding.play.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }
}
