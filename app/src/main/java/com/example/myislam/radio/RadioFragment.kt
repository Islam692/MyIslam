package com.example.myislam.radio

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myislam.api.model.Radio
import com.example.myislam.databinding.FragmentHadethBinding
import com.example.myislam.databinding.FragmentRadioBinding
import com.example.myislam.player.PlayService
import java.sql.Connection

class RadioFragment : Fragment() {
    val adapter = RadioAdapter()
    lateinit var viewBinding: FragmentRadioBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentRadioBinding.inflate(layoutInflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.radioRv.adapter = adapter

        adapter.onItemClickPlay = object : RadioAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, item: Radio) {
                startRadioService(item)
            }

        }
        adapter.onItemClickStop = object :RadioAdapter.OnItemClickListener{
            override fun onItemClick(position: Int, item: Radio) {
                startPlayService()
            }

        }

        getChannelsFromApi()
    }

    private fun startRadioService(item: Radio) {
        if (bound)
    }

    override fun onStart() {
        startService()
        bindService()
    }

    private fun bindService() {
        val intent = Intent(activity,PlayService::class.java)
        activity?.bindService(intent,Connection,Context.BIND_AUTO_CREATE)
    }

    private fun startService() {
        val intent = Intent(activity,PlayService::class.java)
        activity?.startService(intent)
    }

    override fun onStop() {
        super.onStop()
        //activity?.unbindService(Connection)
    }
}