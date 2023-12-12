package com.github.mutoxu_n.randomgroupingapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {
    private var _players: List<Player> = listOf()

    private var _group1: List<Player> = listOf()
    val group1 get() = _group1

    private var _group2: List<Player> = listOf()
    val group2 get() = _group2

    private val _updateCount: MutableLiveData<Int> = MutableLiveData(0)
    val updateCount: LiveData<Int> get() = _updateCount

    fun roll() {
        _updateCount.value = _updateCount.value!! + 1
    }
}