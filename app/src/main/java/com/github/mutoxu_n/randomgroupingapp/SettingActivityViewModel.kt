package com.github.mutoxu_n.randomgroupingapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingActivityViewModel: ViewModel() {
    enum class ButtonState {
        ADD, UPDATE, DELETE
    }

    private val _buttonState: MutableLiveData<ButtonState> = MutableLiveData(ButtonState.ADD)
    val buttonState: LiveData<ButtonState> get() = _buttonState

    private val _name: MutableLiveData<String> = MutableLiveData("")
    val name: LiveData<String> get() = _name

    private  val _point: MutableLiveData<Int> = MutableLiveData(0)
    val point: LiveData<Int> get() = _point

    private val _players: MutableLiveData<List<Player>> = MutableLiveData(listOf())
    val players: LiveData<List<Player>> get() = _players

    init {
        getPlayers()
    }

    private fun getPlayers() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val temp = Database.getDatabase().playerDAO().getAll()
                withContext(Dispatchers.Main) {
                    _players.value = temp
                }
            }
        }
    }

    fun buttonClicked() {
        val name = name.value!!
        val pt = point.value!!

        viewModelScope.launch { withContext(Dispatchers.IO) {
            val dao = Database.getDatabase().playerDAO()
            val p = Player(name, pt)
            when(buttonState.value!!) {
                ButtonState.ADD -> {dao.insert(p)}
                ButtonState.UPDATE -> {dao.update(p)}
                ButtonState.DELETE -> {dao.delete(p)}
            }
            withContext(Dispatchers.Main) {
                _name.value = ""
                _point.value = 0
            }
        } }

        getPlayers()
    }

    fun setName(name: String) {
        _name.value = name
        updateButtonState()
        getPlayers()
    }

    fun setPoint(point: Int) {
        _point.value = point
        updateButtonState()
        getPlayers()
    }

    private fun updateButtonState() {
        val name = name.value!!
        val point = point.value!!

        var player: Player? = null
        for(p in players.value!!)
            if(p.name == name)
                player = p

        _buttonState.value =
            if(player == null) ButtonState.ADD
            else if(player.point == point) ButtonState.DELETE
            else ButtonState.UPDATE
    }

    fun setPoint(point: String) {
        try {
            val p = Integer.parseInt(point)
            setPoint(p)
        } catch (_: Exception) {}
    }
}