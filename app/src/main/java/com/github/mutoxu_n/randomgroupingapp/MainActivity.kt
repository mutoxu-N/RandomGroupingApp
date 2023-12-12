package com.github.mutoxu_n.randomgroupingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mutoxu_n.randomgroupingapp.databinding.ActivityMainBinding
import com.github.mutoxu_n.randomgroupingapp.databinding.PlayerListViewBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.getPlayers()
        }

        // RecyclerView
        val llm1 = LinearLayoutManager(applicationContext)
        binding.rvLeft.layoutManager = LinearLayoutManager(applicationContext)
        binding.rvLeft.addItemDecoration(DividerItemDecoration(this, llm1.orientation))
        binding.rvLeft.adapter = PlayerViewAdapter(listOf())

        val llm2 = LinearLayoutManager(applicationContext)
        binding.rvRight.layoutManager = LinearLayoutManager(applicationContext)
        binding.rvRight.adapter = PlayerViewAdapter(listOf())
        binding.rvRight.addItemDecoration(DividerItemDecoration(this, llm2.orientation))

        // binding
        binding.btRoll.setOnClickListener { viewModel.roll() }

        // view model
        viewModel.updateCount.observe(this) {
            binding.rvLeft.adapter = PlayerViewAdapter(viewModel.group1)
            binding.rvRight.adapter = PlayerViewAdapter(viewModel.group2)
        }

        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.setting -> {
                SettingActivity.startActivity(applicationContext, launcher)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private inner class PlayerViewHolder(val binding: PlayerListViewBinding): RecyclerView.ViewHolder(binding.root)
    private inner class PlayerViewAdapter(val data: List<Player>): RecyclerView.Adapter<PlayerViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder
            = PlayerViewHolder(PlayerListViewBinding.inflate(layoutInflater, parent, false))

        override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
            holder.binding.tvName.text = data[position].name
            holder.binding.tvPoint.text = data[position].point.toString()
        }

        override fun getItemCount(): Int = data.size
    }
}