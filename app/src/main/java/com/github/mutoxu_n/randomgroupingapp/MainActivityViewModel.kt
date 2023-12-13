package com.github.mutoxu_n.randomgroupingapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivityViewModel: ViewModel() {
    private var _players: List<Player> = listOf()

    private var _group1: List<Player> = listOf()
    val group1 get() = _group1

    private var _group2: List<Player> = listOf()
    val group2 get() = _group2

    private var _point1: Int = 0
    val point1 get() = _point1

    private var _point2: Int = 0
    val point2 get() = _point2

    private val _updateCount: MutableLiveData<Int> = MutableLiveData(0)
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
        val group1: MutableList<Player> = mutableListOf()
        val group2: MutableList<Player> = mutableListOf()
        val candidates = _players.toMutableList()
        val probabilities: MutableList<Double> = mutableListOf()
        var isG1: Boolean
        var sum1: Int
        var sum2 = 0
        var temp: Double
        var mean: Double
        var sigma: Double

        // random pick
        var idx = (0 until candidates.size).shuffled().first()
        group1.add(candidates[idx])
        sum1 = candidates[idx].point
        candidates.removeAt(idx)
        isG1 = false

        while(candidates.size > 0) {
            // 得点先順で正規分布を設定
            // |sum1 - sum2| に近いプレイヤーが選ばれやすくする
            mean = abs(sum1 - sum2).toDouble()

            // 分散
            temp = .0
            candidates.forEach { p -> temp += (p.point - mean).pow(2.0) }
            sigma = temp / candidates.size

            // 重み計算
            probabilities.removeAll(probabilities)
            candidates.forEach {
                probabilities.add(norm(it.point , mean, sigma))
            }

            // random pick
            temp = probabilities.sum()
            temp *= Math.random() // 目標累積値
            mean = .0 // 累積和を mean に保存する
            for(i in 0 until probabilities.size) {
                mean += probabilities[i]
                if(mean >= temp) {
                    idx = i
                    break
                }
            }

            // 追加
            if(isG1) {
                group1.add(candidates[idx])
                sum1 += candidates[idx].point

            } else {
                group2.add(candidates[idx])
                sum2 += candidates[idx].point
            }
            candidates.removeAt(idx)
            isG1 = !isG1
        }

        // データ・画面更新
        _group1 = group1
        _point1 = sum1
        _group2 = group2
        _point2 = sum2
        _updateCount.value = _updateCount.value!! + 1
    }

    private fun norm(x: Int, mean: Double, sigma: Double): Double {
        return exp((-(x - mean).pow(2)) / 2 / sigma.pow(2)) / sqrt(2 + Math.PI)
    }
}