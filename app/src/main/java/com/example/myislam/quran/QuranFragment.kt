package com.example.myislam.quran

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myislam.Constance
import com.example.myislam.chapterDetails.ChapterDetailsActivity
import com.example.myislam.databinding.FragmentQuranBinding

class QuranFragment : Fragment() {
    lateinit var viewBinding : FragmentQuranBinding
    var names = listOf(
        "الفَاتِحَة",
        "البَقَرَة",
        "آل عِمرَان",
        "النِّسَاء",
        "المَائدة",
        "الأنعَام",
        "الأعرَاف",
        "الأنفَال",
        "التوبَة",
        "يُونس",
        " هُود",
        "يُوسُف",
        "الرَّعْد",
        "إبراهِيم",
        "الحِجْر",
        "النَّحْل",
        "الإسْرَاء",
        "الكهْف",
        " مَريَم",
        "طه",
        "الأنبيَاء",
        "الحَج",
        " المُؤمنون",
        "النُّور",
        "الفُرْقان",
        "الشُّعَرَاء",
        " النَّمْل",
        "القَصَص",
        "العَنكبوت",
        "الرُّوم",
        "لقمان",
        "السجدة",
        "الأحزاب",
        "سبأ",
        "فاطر",
        "يس",
        "الصافات",
        "ص",
        "الزمر",
        "غافر",
        "فصّلت",
        "الشورى",
        "الزخرف",
        "الدّخان",
        "الجاثية",
        "الأحقاف",
        "محمد",
        "الفتح",
        "الحجرات",
        "ق",
        "الذاريات",
        "الطور",
        "النجم",
        "القمر",
        "الرحمن",
        "الواقعة",
        "الحديد",
        "المجادلة",
        "الحشر",
        "الممتحنة",
        "الصف",
        "الجمعة",
        "المنافقون",
        "التغابن",
        "الطلاق",
        "التحريم",
        "الملك",
        "القلم",
        "الحاقة",
        "المعارج",
        "نوح",
        "الجن",
        "المزّمّل",
        "المدّثر",
        "القيامة",
        "الإنسان",
        "المرسلات",
        "النبأ",
        "النازعات",
        "عبس",
        "التكوير",
        "الإنفطار",
        "المطفّفين",
        "الإنشقاق",
        "البروج",
        "الطارق",
        "الأعلى",
        "الغاشية",
        "الفجر",
        "البلد",
        "الشمس",
        "الليل",
        "الضحى",
        "الشرح",
        "التين",
        "العلق",
        "القدر",
        "البينة",
        "الزلزلة",
        "العاديات",
        "القارعة",
        "التكاثر",
        "العصر",
        "الهمزة",
        "الفيل",
        "قريش",
        "الماعون",
        "الكوثر",
        "الكافرون",
        "النصر",
        "المسد",
        "الإخلاص",
        "الفلق",
        "الناس"
    )
    var types = listOf(
        "ك",
        "م",
        "م",
        "م",
        "م",
        "ك",
        "ك",
        "م",
        "م",
        "ك",
        "ك",
        "ك",
        "م",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        " ك",
        "ك",
        "ك",
        "م",
        "ك",
        "م",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "م",
        "م",
        "م",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "م",
        "ك",
        "م",
        "م",
        "م",
        "م",
        "م",
        "م",
        "م",
        "م",
        "م",
        "م",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "م",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "م",
        "م",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "ك",
        "م",
        "ك",
        "ك",
        "م",
        "م"
    )
    var counter = listOf(
        "1",
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "10",
        "11",
        "12",
        "13",
        "14",
        "15",
        "16",
        "17",
        "18",
        "19",
        "20",
        "21",
        "22",
        "23",
        "24",
        "25",
        "26",
        "27",
        "28",
        "29",
        "30",
        "31",
        "32",
        "33",
        "34",
        "35",
        "36",
        "37",
        "38",
        "39",
        "40",
        "41",
        "42",
        "43",
        "44",
        "45",
        "46",
        "47",
        "48",
        "49",
        "50",
        "51",
        "52",
        "53",
        "54",
        "55",
        "56",
        "57",
        "58",
        "59",
        "60",
        "61",
        "62",
        "63",
        "64",
        "65",
        "66",
        "67",
        "68",
        "69",
        "70",
        "71",
        "72",
        "73",
        "74",
        "75",
        "76",
        "77",
        "78",
        "79",
        "80",
        "81",
        "82",
        "83",
        "84",
        "85",
        "86",
        "87",
        "88",
        "89",
        "90",
        "91",
        "92",
        "93",
        "94",
        "95",
        "96",
        "97",
        "98",
        "99",
        "100",
        "101",
        "102",
        "103",
        "104",
        "105",
        "106",
        "107",
        "108",
        "109",
        "110",
        "111",
        "112",
        "113",
        "114"
    )
    var number = listOf(
        "7",
        "286",
        "200",
        "176",
        "120",
        "165",
        "206",
        "75",
        "129",
        "109",
        "123",
        "111",
        "43",
        "52",
        "99",
        "128",
        "111",
        "110",
        "98",
        "135",
        "112",
        "78",
        "118",
        "64",
        "77",
        "227",
        "93",
        "88",
        "69",
        "60",
        "34",
        "30",
        "73",
        "54",
        "45",
        "83",
        "182",
        "88",
        "75",
        "85",
        "54",
        "53",
        "89",
        "59",
        "37",
        "35",
        "38",
        "29",
        "18",
        "45",
        "60",
        "49",
        "62",
        "55",
        "78",
        "96",
        "29",
        "22",
        "24",
        "13",
        "14",
        "11",
        "11",
        "18",
        "12",
        "12",
        "30",
        "52",
        "52",
        "44",
        "28",
        "28",
        "20",
        "56",
        "40",
        "31",
        "50",
        "40",
        "46",
        "42",
        "29",
        "19",
        "36",
        "25",
        "22",
        "17",
        "19",
        "26",
        "30",
        "20",
        "15",
        "21",
        "11",
        "8",
        "8",
        "19",
        "5",
        "8",
        "8",
        "11",
        "11",
        "8",
        "3",
        "9",
        "5",
        "4",
        "7",
        "3",
        "6",
        "3",
        "5",
        "5",
        "5",
        "6"
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentQuranBinding.inflate(layoutInflater,container,false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initeRecyclerViewName()
    }


    lateinit var adapter: ChapterNameRecyclerAdapter
    private fun initeRecyclerViewName() {
        adapter = ChapterNameRecyclerAdapter(names,counter,number,types)
        adapter.onItemClickListener =
            ChapterNameRecyclerAdapter.OnItemClickListener { position, name ->
                startChapterDetailsScreen(position,name, counter , number)
            }
        viewBinding.recyclerView.adapter = adapter
    }

    private fun startChapterDetailsScreen(index: Int, name:String, number: List<String>, counter: List<String>) {
        val intent = Intent(context,ChapterDetailsActivity::class.java)
        intent.putExtra(Constance.EXTRA_CHAPTER_INDEX , index+1)
        intent.putExtra(Constance.EXTRA_CHAPTER_NAME , name)
        intent.putExtra(Constance.EXTRA_CHAPTER_NUMBER , number[index])
        intent.putExtra(Constance.EXTRA_CHAPTER_COUNTER , counter[index])
        startActivity(intent)
    }
}