package com.example.mychatapp.Setting

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mychatapp.R

class SettingFragment: Fragment() {
//    private lateinit var to_sfu: Preference
//    private lateinit var to_course: Preference
//    private lateinit var to_github: Preference
//    private lateinit var to_up: Preference

    private lateinit var theUserProfile: ImageView

    private lateinit var imageView1:ImageView
    private lateinit var textView1:TextView

    private lateinit var imageView2:ImageView
    private lateinit var textView2:TextView

    private lateinit var imageView3:ImageView
    private lateinit var textView3:TextView

    private lateinit var imageView00:ImageView
    private lateinit var textView00:TextView
    private lateinit var textView01:TextView
    private lateinit var imageView01:ImageView

    @SuppressLint("MissingInflatedId", "CutPasteId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        imageView1 = view.findViewById<ImageView>(R.id.imgCourseWebPage)
        textView1 = view.findViewById<TextView>(R.id.textCourseWebPage)
        imageView1.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse("https://www.sfu.ca/~xingdong/Teaching/CMPT362/web/cs65.html")
            startActivity(intent); }
        textView1.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse("https://www.sfu.ca/~xingdong/Teaching/CMPT362/web/cs65.html")
            startActivity(intent); }


        imageView2 = view.findViewById<ImageView>(R.id.imgSFUWebPage)
        textView2 = view.findViewById<TextView>(R.id.textSFUWebPage)
        imageView2.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse("http://www.sfu.ca/computing.html")
            startActivity(intent); }
        textView2.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse("http://www.sfu.ca/computing.html")
            startActivity(intent); }


        imageView3 = view.findViewById<ImageView>(R.id.imgGitHubWebPage)
        textView3 = view.findViewById<TextView>(R.id.textGitHubWebPage)
        imageView3.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse("https://github.com/Gravesky/cmpt362_final_project")
            startActivity(intent); }
        textView3.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse("https://github.com/Gravesky/cmpt362_final_project")
            startActivity(intent); }


        imageView00 = view.findViewById<ImageView>(R.id.imgMe)
        textView00 = view.findViewById<TextView>(R.id.text_view_Me_name)
        textView01 = view.findViewById<TextView>(R.id.text_view_Me_AccID)
        imageView01 = view.findViewById<ImageView>(R.id.imgMe_Arrow)
        imageView00.setOnClickListener {toUserProfile(view)}
        textView00.setOnClickListener {toUserProfile(view)}
        textView01.setOnClickListener {toUserProfile(view)}
        imageView01.setOnClickListener {toUserProfile(view)}


//        to_up = findPreference("TO_UserProfile")!!
//
//        to_up.setOnPreferenceClickListener {
//
//            val jumpToShow:Intent = Intent(context, ContactActivity::class.java)
//            startActivity(jumpToShow)
//
//            true
//        }
//
//        theUserProfile.findViewById<View>(R.id.imageUserProfile)
//        theUserProfile.setImageResource(R.drawable.sfu_logo)
//
//
//
//
//        to_course = findPreference("TO_COURSE")!!
//        to_course.setOnPreferenceClickListener {
//
//            val intent = Intent(Intent.ACTION_VIEW)
//            intent.action = "android.intent.action.VIEW"
//            intent.data = Uri.parse("https://www.sfu.ca/~xingdong/Teaching/CMPT362/web/cs65.html")
//            startActivity(intent);
//
//            true
//        }
//
//        to_sfu = findPreference("TO_SFU")!!
//        to_sfu.setOnPreferenceClickListener {
//
//            val intent = Intent(Intent.ACTION_VIEW)
//            intent.action = "android.intent.action.VIEW"
//            intent.data = Uri.parse("http://www.sfu.ca/computing.html")
//            startActivity(intent);
//
//            true
//        }
//
//        to_github = findPreference("TO_GITHUB")!!
//        to_github.setOnPreferenceClickListener {
//
//            val intent = Intent(Intent.ACTION_VIEW)
//            intent.action = "android.intent.action.VIEW"
//            intent.data = Uri.parse("https://github.com/Gravesky/cmpt362_final_project")
//            startActivity(intent);
//
//            true
//        }


        return view
    }

//    fun sss(view: View){
//        val jumpToShow:Intent = Intent(requireContext(), ContactActivity::class.java)
//        startActivity(jumpToShow)
//    }

//    val jumpToShow:Intent = Intent(requireContext(), ContactActivity::class.java)
//    startActivity(jumpToShow)

    fun toUserProfile(view: View){
        val jumpToShow:Intent = Intent(context, UserProfileActivity::class.java)
        startActivity(jumpToShow)
    }
}