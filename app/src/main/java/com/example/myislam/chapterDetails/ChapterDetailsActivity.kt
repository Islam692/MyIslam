package com.example.myislam.chapterDetails

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.myislam.Constance
import com.example.myislam.R
import com.example.myislam.databinding.ActivityChapterDetailsBinding

class ChapterDetailsActivity : AppCompatActivity() {
    lateinit var viewBinding : ActivityChapterDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityChapterDetailsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initeParams()
        initViews()
        loadChapterFile()
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.colorr)
        }
    }

    private fun initViews() {
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewBinding.content.suraName.text = chapterName
        viewBinding.content.suraCountern.text = chapterCounter
        viewBinding.content.suraFehresn.text = chapterNumber
        supportActionBar?.title = ""
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun loadChapterFile() {
        val filecontent = assets.open("$chapterIndex.txt").bufferedReader().use { it.readText() }
        val lines = filecontent.trim().split("\n")
        bindVerses(lines)
    }

    lateinit var adapter: VersesAdapter
    private fun bindVerses(verses: List<String>,) {
        adapter = VersesAdapter(verses)
        viewBinding.content.versesRecyclerView.adapter = adapter
    }

    lateinit var chapterName : String
    lateinit var chapterNumber : String
    lateinit var chapterCounter : String
    var chapterIndex : Int = 0
    private fun initeParams() {
        chapterName  = intent.getStringExtra(Constance.EXTRA_CHAPTER_NAME)?:""
        chapterIndex  = intent.getIntExtra(Constance.EXTRA_CHAPTER_INDEX , 0)
        chapterNumber  = intent.getStringExtra(Constance.EXTRA_CHAPTER_NUMBER)!!
        chapterCounter  = intent.getStringExtra(Constance.EXTRA_CHAPTER_COUNTER)!!
    }
}