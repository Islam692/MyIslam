package com.example.myislam.radio

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.myislam.Constance
import com.example.myislam.R
import com.example.myislam.api.ApiManager
import com.example.myislam.api.Radio
import com.example.myislam.api.RadioResponse
import com.example.myislam.databinding.FragmentRadioBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RadioFragment : Fragment() {
    private lateinit var binding: FragmentRadioBinding
    private var _mediaPlayer: MediaPlayer? = null
    private val mediaPlayer: MediaPlayer get() = _mediaPlayer!!
    private var mediaPlayerAvailable = false
    private lateinit var radiosList: List<Radio>
    private var currentlyPlaying = false
    private var currentRadioIndex = 0
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRadioBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences(
            Constance.SETTINGS_FILE_NAME,
            Context.MODE_PRIVATE
        )

        retrieveSavedData()

        loadRadios()

        binding.play.setOnClickListener {
            if (!mediaPlayerAvailable) {
                Toast.makeText(requireContext(), "media player not available", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (currentlyPlaying) {
                mediaPlayer.pause()
                togglePlayingStatus(false)
            } else {
                mediaPlayer.start()
                togglePlayingStatus(true)
            }
        }

        binding.next.setOnClickListener { playNextRadio() }
        binding.previous.setOnClickListener { playPreviousRadio() }
    }

    private fun retrieveSavedData() {
        val name =
            sharedPreferences.getString(Constance.SAVED_RADIO_NAME, "إذاعـة القـرآن الـكـريـم")
        val url: String = sharedPreferences.getString(Constance.SAVED_RADIO_URL, "") ?: ""
        Log.d("tt", "name $name, url $url")
        if (url.isNotEmpty()) initMediaPlayer(name, url)
    }

    private fun playPreviousRadio() {
        mediaPlayerAvailable = false
        togglePlayingVisibility(false)
        togglePlayingStatus(false)

        currentRadioIndex = if (currentRadioIndex == 0) radiosList.size - 1 else --currentRadioIndex
        playRadioAtCurrentIndex()
    }

    private fun playNextRadio() {
        mediaPlayerAvailable = false
        togglePlayingVisibility(false)
        togglePlayingStatus(false)

        currentRadioIndex = if (currentRadioIndex == radiosList.size - 1) 0 else ++currentRadioIndex
        playRadioAtCurrentIndex()
    }

    private fun playRadioAtCurrentIndex() {
        mediaPlayer.apply {
            reset()
            setDataSource(radiosList[currentRadioIndex].url)
            prepareAsync()
            setOnPreparedListener {
                mediaPlayerAvailable = true
                binding.izaaTv.text = radiosList[currentRadioIndex].name
                togglePlayingVisibility(true)
                start()
                togglePlayingStatus(true)
            }
        }

        saveRadioData()
    }

    private fun getCurrentLanguageCode(): String {
        return if (resources.configuration.locales[0].language == "ar") Constance.ARABIC_LANG_CODE
        else Constance.ENGLISH_LANG_CODE
    }

    private fun loadRadios() {
        togglePlayingVisibility(false)

        ApiManager.getRadiosService()
            .getRadios(language = getCurrentLanguageCode())
            .enqueue(object : Callback<RadioResponse> {
                override fun onResponse(
                    call: Call<RadioResponse>,
                    response: Response<RadioResponse>
                ) {
                    if (response.isSuccessful) {
                        radiosList = response.body()?.radios ?: emptyList()
                        Log.d("tt", "radios list ready")
                        if (_mediaPlayer == null) initMediaPlayer()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "error occurred: ${response.errorBody().toString()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(p0: Call<RadioResponse>, p1: Throwable) {
                    Toast.makeText(requireContext(), p1.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun initMediaPlayer(name: String? = null, url: String? = null) {
        _mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(url ?: radiosList[currentRadioIndex].url)
            prepareAsync()
            setOnPreparedListener {
                mediaPlayerAvailable = true
                binding.izaaTv.text = name ?: radiosList[currentRadioIndex].name
                togglePlayingVisibility(true)
                start()
                togglePlayingStatus(true)
            }
        }
    }

    private fun togglePlayingVisibility(playing: Boolean) {
        binding.play.isVisible = playing
        binding.loadingProgress.isVisible = !playing
    }

    private fun togglePlayingStatus(currentlyPlaying: Boolean) {
        this.currentlyPlaying = currentlyPlaying
        if (currentlyPlaying) binding.play.setImageResource(R.drawable.baseline_play_arrow_24)
        else binding.play.setImageResource(R.drawable.ic_pause)
    }


    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        _mediaPlayer = null
    }

    private fun saveRadioData() {
        val radio = radiosList[currentRadioIndex]
        sharedPreferences.edit().putString(Constance.SAVED_RADIO_NAME, radio.name).apply()
        sharedPreferences.edit().putString(Constance.SAVED_RADIO_URL, radio.url).apply()
    }
}