package com.example.myislam.radio

import android.Manifest
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.myislam.Constants
import com.example.myislam.R
import com.example.myislam.api.Radio
import com.example.myislam.databinding.FragmentRadioBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class RadioFragment : Fragment() {
    private lateinit var binding: FragmentRadioBinding
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var radioPlayerService: RadioPlayerService
    private var isRadioPlayerServiceBound: Boolean = false

    private lateinit var notificationPermissionRequestLauncher: ActivityResultLauncher<String>
    private lateinit var notificationPermissionDialog: AlertDialog
    private lateinit var notificationChannelDialog: AlertDialog
    private var settingsOpenedToEnableChannel = false


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


    private fun isNotificationChannelEnabled(): Boolean {
        val notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = notificationManager.getNotificationChannel(Constants.CHANNEL_ID)
        return notificationChannel?.importance != NotificationManager.IMPORTANCE_NONE
    }


    private fun openSettingsToEnableNotificationChannel() {
        val notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = notificationManager.getNotificationChannel(Constants.CHANNEL_ID)

        val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, Constants.PACKAGE_NAME)
            putExtra(Settings.EXTRA_CHANNEL_ID, notificationChannel.id)
        }
        startActivity(intent)
        settingsOpenedToEnableChannel = true
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences(
            Constants.SETTINGS_FILE_NAME,
            Context.MODE_PRIVATE
        )

        notificationPermissionDialog = createDialog(
            "Permission Required",
            "In order to play the radios, we need the notification permission",
            "Request again",
            {
                notificationPermissionRequestLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            },
            "No thanks"
        )

        notificationChannelDialog = createDialog(
            "Notification Channel Disabled",
            "The notifications channel needs to be enabled before sending notifications.",
            "Enable",
            {
                openSettingsToEnableNotificationChannel()
            },
            "No thanks"
        )

        notificationPermissionRequestLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { permissionGranted ->
            if (!permissionGranted) return@registerForActivityResult

            if (isNotificationChannelEnabled()) initRadioFragment()
            else notificationChannelDialog.show()
        }

        // Request permission (or show rationale) if not granted
        handleNotificationPermissionAndChannel()

        binding.btnGrantPermission.setOnClickListener {
            notificationPermissionRequestLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }

        binding.btnEnableChannel.setOnClickListener { openSettingsToEnableNotificationChannel() }

        binding.play.setOnClickListener { toggleRadioPlayer() }
        binding.next.setOnClickListener { playNextRadio() }
        binding.previous.setOnClickListener { playPreviousRadio() }
    }


    override fun onResume() {
        super.onResume()
        // check channel status on return
        if (settingsOpenedToEnableChannel) {
            if (isNotificationChannelEnabled()) showMainFragmentUI()
        }
    }

    private fun handleNotificationPermissionAndChannel(): Boolean {
        return when {
            !isNotificationChannelEnabled() -> {
                showDisabledChannelUI()
                notificationChannelDialog.show()
                false
            }

            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                initRadioFragment()
                true
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.POST_NOTIFICATIONS
            ) -> {
                notificationPermissionDialog.show()
                false
            }

            else -> {
                notificationPermissionRequestLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                false
            }
        }
    }

    private fun initRadioFragment() {
        // show proper UI and start service
        showMainFragmentUI()
        initRadioService()
    }

    private fun showMainFragmentUI() {
        binding.mainUiDesign.isVisible = true
        binding.noPermissionUiDesign.isVisible = false
        binding.disabledChannelUiDesign.isVisible = false
    }

    private fun showDisabledChannelUI() {
        binding.mainUiDesign.isVisible = false
        binding.noPermissionUiDesign.isVisible = false
        binding.disabledChannelUiDesign.isVisible = true
    }

    private fun createDialog(
        title: String,
        message: String,
        posBtnText: String? = null,
        posBtnAction: (() -> Unit)? = null,
        negBtnText: String? = null,
        negBtnAction: (() -> Unit)? = null,
        isCancelable: Boolean = false
    ): AlertDialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(posBtnText) { dialog, _ ->
                posBtnAction?.invoke()
            }
            .setNegativeButton(negBtnText) { dialog, _ ->
                negBtnAction?.invoke()
            }
            .setCancelable(isCancelable)
            .create()
    }

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

            override fun onPaused(radio: Radio) {
                togglePlayingVisibility(true)
                togglePlayingStatus(false)
                binding.izaaTv.text = radio.name
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
        if (!handleNotificationPermissionAndChannel()) return

        if (isRadioPlayerServiceBound) {
            radioPlayerService.playOrPauseRadio()
        }
    }

    private fun playPreviousRadio() {
        if (!handleNotificationPermissionAndChannel()) return

        if (isRadioPlayerServiceBound) {
            radioPlayerService.playPreviousRadio()
            togglePlayingVisibility(false)
            togglePlayingStatus(false)
        }
    }

    private fun playNextRadio() {
        if (!handleNotificationPermissionAndChannel()) return

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
