package com.example.myislam.chapterDetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myislam.Constance
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
    }

    private fun initViews() {
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewBinding.content.suraName.text = chapterName
        supportActionBar?.title = ""
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
    var chapterIndex : Int = 0
    private fun initeParams() {
        chapterName  = intent.getStringExtra(Constance.EXTRA_CHAPTER_NAME)?:""
        chapterIndex  = intent.getIntExtra(Constance.EXTRA_CHAPTER_INDEX , 0)
    }
}