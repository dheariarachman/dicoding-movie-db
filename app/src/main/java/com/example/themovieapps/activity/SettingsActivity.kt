package com.example.themovieapps.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.themovieapps.CatalogueReceiver
import com.example.themovieapps.R
import java.text.SimpleDateFormat
import java.util.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        private lateinit var catalogueReceiver: CatalogueReceiver

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            catalogueReceiver = CatalogueReceiver()
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            if (preference?.key == context?.getString(R.string.key_daily)) {
                val statusDaily: Boolean? = preference?.sharedPreferences?.getBoolean(
                    resources.getString(R.string.key_release),
                    false
                )
                if (statusDaily != null) {
                    if (statusDaily) {
                        val hourOfDay = 0
                        val minuteOfDay = 0
                        val second = 5
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minuteOfDay)
                        calendar.set(Calendar.SECOND, second)
                        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                        catalogueReceiver.setRepeatingNotification(
                            context,
                            CatalogueReceiver.TYPE_REPEAT,
                            dateFormat.format(calendar.time),
                            resources.getString(R.string.lipsum)
                        )
                    } else {
                        Toast.makeText(context, "Di Batalkan", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            return super.onPreferenceTreeClick(preference)
        }
    }
}