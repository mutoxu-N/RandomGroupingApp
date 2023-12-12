package com.github.mutoxu_n.randomgroupingapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel: ViewModel() {
    private var _players: List<Player> = listOf()

    private var _group1: List<Player> = listOf()
    val group1 get() = _group1

    private var _group2: List<Player> = listOf()

    private var _point1: Int = 0
    val point1 get() = _point1

    val group2 get() = _group2

    private val _updateCount: MutableLiveData<Int> = MutableLiveData(0)

    private var _point2: Int = 0
    val point2 get() = _point2

    val updateCount: LiveData<Int> get() = _updateCount

    init {
        getPlayers()
    }

    fun getPlayers() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _players = Database.getDatabase().playerDAO().getAll()
            }
        }
    }

    fun roll() {
        // TODO: 重み付きでランダム振り分け
        val candidates = _players.toMutableList()


        // 各グループのポイント計算
        var sum = 0
        group1.forEach { p -> sum += p.point }
        _point1 = sum

        sum = 0
        group2.forEach { p -> sum += p.point }
        _point2 = sum

        _updateCount.value = _updateCount.value!! + 1
    }
}