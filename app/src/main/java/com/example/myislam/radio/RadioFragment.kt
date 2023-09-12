package com.example.myislam.radio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myislam.databinding.FragmentHadethBinding
import com.example.myislam.databinding.FragmentRadioBinding

class RadioFragment : Fragment() {
    lateinit var viewBinding : FragmentRadioBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentRadioBinding.inflate(layoutInflater,container,false)
        return viewBinding.root
    }
}