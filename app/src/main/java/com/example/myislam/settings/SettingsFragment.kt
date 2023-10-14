package com.example.myislam.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.myislam.Constance
import com.example.myislam.R
import com.example.myislam.chapterDetails.ChapterDetailsActivity
import com.example.myislam.databinding.FragmentQuranBinding
import com.example.myislam.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    lateinit var viewBinding : FragmentSettingsBinding
    lateinit var spinner : Spinner
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSettingsBinding.inflate(layoutInflater,container,false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}