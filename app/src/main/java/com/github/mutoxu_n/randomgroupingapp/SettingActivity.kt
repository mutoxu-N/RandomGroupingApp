package com.github.mutoxu_n.randomgroupingapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mutoxu_n.randomgroupingapp.databinding.ActivitySettingBinding
import com.github.mutoxu_n.randomgroupingapp.databinding.PlayerListViewBinding

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

        // Recycler View
        val llm = LinearLayoutManager(applicationContext)
        binding.rvPlayers.layoutManager = llm
        binding.rvPlayers.adapter = PlayerViewAdapter(listOf())
        binding.rvPlayers.addItemDecoration(DividerItemDecoration(this, llm.orientation))

        // binding
        binding.btn.setOnClickListener { viewModel.buttonClicked() }
        binding.etName.doOnTextChanged { text, _, _, _ ->
            viewModel.setName(text.toString())
        }
        binding.etPoint.doOnTextChanged { text, _, _, _ ->
            viewModel.setPoint(text.toString())
        }

        // view model
        viewModel.players.observe(this) {it?.let {
            binding.rvPlayers.adapter = PlayerViewAdapter(it)
        } }
        viewModel.name.observe(this) {it?.let {
            if(binding.etName.text.toString() != it)
                binding.etName.setText(it)
        }}
        viewModel.point.observe(this){it?.let {
            if(binding.etPoint.text.toString() != it.toString())
                binding.etPoint.setText(it.toString())
        }}

        viewModel.buttonState.observe(this) { it?.let {
            when(it) {
                SettingActivityViewModel.ButtonState.ADD -> {
                    binding.btn.text = resources.getString(R.string.add) }
                SettingActivityViewModel.ButtonState.UPDATE -> {
                    binding.btn.text = resources.getString(R.string.update)}
                SettingActivityViewModel.ButtonState.DELETE -> {
                    binding.btn.text = resources.getString(R.string.delete)}
            }
        }}

        setContentView(binding.root)
    }

    private inner class PlayerViewHolder(val binding: PlayerListViewBinding): RecyclerView.ViewHolder(binding.root)
    private inner class PlayerViewAdapter(val data: List<Player>): RecyclerView.Adapter<PlayerViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder
            = PlayerViewHolder(PlayerListViewBinding.inflate(layoutInflater, parent, false))

        override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
            holder.binding.tvName.text = data[position].name
            holder.binding.tvPoint.text = data[position].point.toString()
            holder.binding.root.setOnClickListener {
                viewModel.setName(data[position].name)
                viewModel.setPoint(data[position].point)
            }
        }

        override fun getItemCount(): Int = data.size
    }
}