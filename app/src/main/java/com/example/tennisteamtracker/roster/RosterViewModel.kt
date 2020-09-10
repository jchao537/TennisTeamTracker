package com.example.tennisteamtracker.roster

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.tennisteamtracker.database.PlayerDatabaseDao
import com.example.tennisteamtracker.formatPlayers
import kotlinx.coroutines.*
import timber.log.Timber

class RosterViewModel(val database: PlayerDatabaseDao, application: Application) : AndroidViewModel(application) {
    private val players = database.getAllPlayersAsLiveData()
    val playersString = Transformations.map(players) { players ->
        formatPlayers(players, application.resources)
    }

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToAdd = MutableLiveData<Boolean>()
    val navigateToAdd: LiveData<Boolean>
        get() = _navigateToAdd

    fun onFabClicked() {
        _navigateToAdd.value = true
    }

    fun onNavigatedToAdd() {
        _navigateToAdd.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onClear() {
        Timber.i("Ready to clear")
        uiScope.launch {
            // Clear the database table.
            clear()
        }
        Timber.i("Cleared")
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clearAllPlayers()
        }
    }
}
