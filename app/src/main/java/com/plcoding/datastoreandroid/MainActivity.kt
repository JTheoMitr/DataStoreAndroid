package com.plcoding.datastoreandroid

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
                UserStorage().run {
                    dataStore = createDataStore(name = "settings")
                    save("Name",
                        binding.etSaveKey.text.toString()) }

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
        }

        binding.btnRead.setOnClickListener {
            lifecycleScope.launch {
                val value = UserStorage().run {
                    read(binding.etReadkey.text.toString())
                }
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