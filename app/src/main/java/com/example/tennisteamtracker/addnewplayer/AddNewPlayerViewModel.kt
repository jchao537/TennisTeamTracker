package com.example.tennisteamtracker.addnewplayer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tennisteamtracker.database.Player
import com.example.tennisteamtracker.database.PlayerDatabaseDao
import kotlinx.coroutines.*
import timber.log.Timber

class AddNewPlayerViewModel(val database: PlayerDatabaseDao, application: Application) : AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var newPlayer = MutableLiveData<Player?>()

    private val _navigateToRoster = MutableLiveData<Boolean>()
    val navigateToRoster: LiveData<Boolean>
        get() = _navigateToRoster

    fun onSaveClicked() {
        _navigateToRoster.value = true
    }

    fun onNavigatedToRoster() {
        _navigateToRoster.value = false
    }

    fun saveNewPlayer(player: Player) {
        viewModelScope.launch{
            insert(player)
            Timber.i("New Player Added 1")
        }
    }

    private suspend fun insert(player: Player) {
        withContext(Dispatchers.IO) {
            database.insertPlayer(player)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}