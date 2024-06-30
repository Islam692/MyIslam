package com.example.myislam.ui.home.fragments.radio

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
import com.example.myislam.R
import com.example.myislam.data.radio_api.models.Radio
import com.example.myislam.databinding.FragmentRadioBinding
import com.example.myislam.utils.Constants
import com.example.myislam.utils.Utils
import com.example.myislam.utils.Utils.Companion.createDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@AndroidEntryPoint
class RadioFragment : Fragment() {
    private lateinit var binding: FragmentRadioBinding
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var radioPlayerService: RadioPlayerService
    private var isRadioPlayerServiceBound: Boolean = false
    private var serviceStopped = false

    private lateinit var notificationPermissionRequestLauncher: ActivityResultLauncher<String>
    private lateinit var notificationPermissionDialog: AlertDialog
    private lateinit var notificationChannelDialog: AlertDialog
    private var settingsOpenedToEnableChannel = false

    @Inject
    lateinit var utils: Utils


    private val radioPlayerServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as RadioPlayerService.RadioPlayerBinder
            radioPlayerService = binder.getService()
            isRadioPlayerServiceBound = true
            defineRadioPlayerServiceCallback()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isRadioPlayerServiceBound = false
            serviceStopped = true
//            // restart service if disconnected
//            startRadioPlayerService()
        }
    }


    override fun onStart() {
        super.onStart()
        startRadioPlayerService()
    }

    private fun startRadioPlayerService() {
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
            putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
            putExtra(Settings.EXTRA_CHANNEL_ID, notificationChannel.id)
        }
        startActivity(intent)
        settingsOpenedToEnableChannel = true
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeSharedPreferences()
        defineDialogs()
        defineNotificationPermissionRequestCallback()
        setButtonsClickListeners()

        // Request permission (or show rationale) if not granted
        handleNotificationPermissionAndChannel()
    }

    private fun setButtonsClickListeners() {
        binding.btnGrantPermission.setOnClickListener { requestNotificationPermission() }
        binding.btnEnableChannel.setOnClickListener { openSettingsToEnableNotificationChannel() }
        binding.playPauseButton.setOnClickListener { toggleRadioPlayer() }
        binding.next.setOnClickListener { playNextRadio() }
        binding.previous.setOnClickListener { playPreviousRadio() }
    }

    private fun defineNotificationPermissionRequestCallback() {
        notificationPermissionRequestLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { permissionGranted ->
            if (!permissionGranted) return@registerForActivityResult

            if (isNotificationChannelEnabled()) initRadioFragment()
            else notificationChannelDialog.show()
        }
    }

    private fun initializeSharedPreferences() {
        sharedPreferences = requireActivity().getSharedPreferences(
            Constants.SETTINGS_FILE_NAME,
            Context.MODE_PRIVATE
        )
    }

    private fun defineDialogs() {
        notificationPermissionDialog = createDialog(
            title = getString(R.string.permission_required_label),
            message = getString(R.string.permission_request_message),
            posBtnText = getString(R.string.request_again),
            posBtnAction = {
                requestNotificationPermission()
            },
            negBtnText = getString(R.string.no_thanks)
        )

        notificationChannelDialog = createDialog(
            title = getString(R.string.notification_channel_disabled),
            message = getString(R.string.notification_channel_dialog_message),
            posBtnText = getString(R.string.enable),
            posBtnAction = {
                openSettingsToEnableNotificationChannel()
            },
            negBtnText = getString(R.string.no_thanks)
        )
    }


    override fun onResume() {
        super.onResume()
        // check channel status on return
        handleNotificationPermissionAndChannel()
    }

    private fun handleNotificationPermissionAndChannel(): Boolean {
        return when {
            !isNotificationChannelEnabled() -> {
                showDisabledChannelUI()
                notificationChannelDialog.show()
                false
            }

            isPermissionGranted(Manifest.permission.POST_NOTIFICATIONS) -> {
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
                requestNotificationPermission()
                false
            }
        }
    }

    private fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestNotificationPermission() {
        notificationPermissionRequestLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
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

    private fun initRadioService() {
        val serviceIntent = Intent(requireContext(), RadioPlayerService::class.java)
        serviceIntent.putExtra(Constants.START_ACTION, Constants.INIT_SERVICE)
        requireActivity().startForegroundService(serviceIntent)

        showLoadingBar()
        showPauseButton()
    }

    private fun defineRadioPlayerServiceCallback() {
        radioPlayerService.setRadioPlayerCallback(object :
            RadioPlayerService.RadioPlayerCallback {
            override fun onPlayed(radio: Radio) {
                showPlayPauseButton()
                showPauseButton()
                binding.radioNameTextView.text = radio.name
            }

            override fun onPaused(radio: Radio) {
                showPlayPauseButton()
                showPlayButton()
                binding.radioNameTextView.text = radio.name
            }

            override fun onNextPlayed(radio: Radio) {
                showPlayPauseButton()
                showPauseButton()
                binding.radioNameTextView.text = radio.name
            }

            override fun onPreviousPlayed(radio: Radio) {
                showPlayPauseButton()
                showPauseButton()
                binding.radioNameTextView.text = radio.name
            }

            override fun onLoading() {
                showLoadingBar()
            }

            override fun onServiceStopped() {
                serviceStopped = true
                utils.showShortToast(getString(R.string.radio_service_stopped))
            }
        })
    }

    private fun toggleRadioPlayer() {
        if (!handleNotificationPermissionAndChannel()) return
        if (serviceStopped) {
            initRadioService()
            utils.showShortToast(getString(R.string.restarting_radio_service))
            return
        }

        if (isRadioPlayerServiceBound) {
            radioPlayerService.playOrPauseRadio()
        }
    }

    private fun playPreviousRadio() {
        if (!handleNotificationPermissionAndChannel()) return
        if (serviceStopped) {
            initRadioService()
            utils.showShortToast(getString(R.string.restarting_radio_service))
            return
        }

        if (isRadioPlayerServiceBound) radioPlayerService.playPreviousRadio()
    }

    private fun playNextRadio() {
        if (!handleNotificationPermissionAndChannel()) return
        if (serviceStopped) {
            initRadioService()
            utils.showShortToast(getString(R.string.restarting_radio_service))
            return
        }

        if (isRadioPlayerServiceBound) radioPlayerService.playNextRadio()
    }

    private fun showLoadingBar() {
        binding.playPauseButton.isVisible = false
        binding.loadingProgress.isVisible = true
    }

    private fun showPlayPauseButton() {
        binding.playPauseButton.isVisible = true
        binding.loadingProgress.isVisible = false
    }

    private fun showPlayButton() {
        binding.playPauseButton.setImageResource(R.drawable.ic_play_gold)
        binding.playPauseButton.scaleType = ImageView.ScaleType.CENTER
    }

    private fun showPauseButton() {
        binding.playPauseButton.setImageResource(R.drawable.ic_pause)
        binding.playPauseButton.scaleType = ImageView.ScaleType.CENTER_CROP
    }
}
