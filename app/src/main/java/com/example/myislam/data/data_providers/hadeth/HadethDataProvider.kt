package com.example.myislam.data.data_providers.hadeth

import android.app.Activity
import com.example.myislam.data.models.Hadeth

object HadethDataProvider {

    fun getHadethList(activity: Activity): List<Hadeth> {
        // Read hadeth file
        val hadethList = mutableListOf<Hadeth>()
        val fileContent = activity.assets.open("ahadeth.txt").bufferedReader().use { it.readText() }
        val ahadethList = fileContent.trim().split("#")

        // Construct hadeth list
        ahadethList.forEach { hadeth ->
            val lines = hadeth.trim().split("\n")
            val title = lines[0]
            val content = lines.joinToString("\n")
            hadethList.add(Hadeth(title, content))
        }

        return hadethList
    }
}