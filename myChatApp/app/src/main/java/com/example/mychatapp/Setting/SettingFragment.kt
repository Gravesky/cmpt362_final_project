package com.example.mychatapp.Setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.mychatapp.R

class SettingFragment: PreferenceFragmentCompat() {
    private lateinit var to_sfu: Preference
    private lateinit var to_course: Preference
    private lateinit var to_github: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences,rootKey)


        to_course = findPreference("TO_COURSE")!!
        //to_course.summary = "1111111"
        to_course.setOnPreferenceClickListener {

            val intent = Intent(Intent.ACTION_VIEW)
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse("https://www.sfu.ca/~xingdong/Teaching/CMPT362/web/cs65.html")
            startActivity(intent);

            true
        }

        to_sfu = findPreference("TO_SFU")!!
        to_sfu.setOnPreferenceClickListener {

            val intent = Intent(Intent.ACTION_VIEW)
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse("http://www.sfu.ca/computing.html")
            startActivity(intent);

            true
        }

        to_github = findPreference("TO_GITHUB")!!
        to_github.setOnPreferenceClickListener {

            val intent = Intent(Intent.ACTION_VIEW)
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse("https://github.com/Gravesky/cmpt362_final_project")
            startActivity(intent);

            true
        }

    }
}