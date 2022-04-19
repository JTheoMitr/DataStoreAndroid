package com.plcoding.datastoreandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import com.plcoding.datastoreandroid.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.btnSave.setOnClickListener {
            lifecycleScope.launch {
                    dataStore = createDataStore(name = "settings")
                    save("refresh_token",
                        binding.etSaveKey.text.toString())
                // will be data.refresh_token after we add getTokenData (not binding.etSaveKey)

                //Allow User to define key and field:
//                save(
//                    binding.etSaveKey.text.toString(),
//                    binding.etSaveKey.text.toString()
//                )

                //Pre-defined Keys:
//                save(
//                    "Name",
//                    binding.etSaveKey.text.toString()
//                )
//
//                save(
//                    "Age",
//                        binding.etSaveValue.text.toString()
//                )
            }
            val intent = Intent(this, TokenActivity::class.java)
            startActivity(intent)
        }

        binding.btnRead.setOnClickListener {
            lifecycleScope.launch {
                val value = read(binding.etReadkey.text.toString())
                            //will be read("refresh_token")

                binding.tvReadValue.text = value ?: "No value found"
            }
        }
    }

    private suspend fun save(key: String, value: String) {
        val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    private suspend fun read(key: String): String? {
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}