package com.github.mutoxu_n.randomgroupingapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModelProvider
import com.github.mutoxu_n.randomgroupingapp.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    private lateinit var viewModel: SettingActivityViewModel

    companion object {
        fun startActivity(context: Context, launcher: ActivityResultLauncher<Intent>) {
            val intent = Intent(context, SettingActivity::class.java)
            launcher.launch(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[SettingActivityViewModel::class.java]

        setContentView(binding.root)
    }
}