package com.example.tennisteamtracker.addgameday

import android.app.Application
import androidx.lifecycle.*
import com.example.tennisteamtracker.database.Game
import com.example.tennisteamtracker.database.GameDayDatabaseDao
import com.example.tennisteamtracker.database.Player
import com.example.tennisteamtracker.database.PlayerDatabaseDao
import com.example.tennisteamtracker.formatPlayers
import kotlinx.coroutines.*
import timber.log.Timber

class AddGameDayViewModel(val database: GameDayDatabaseDao, val playerDatabase: PlayerDatabaseDao, application: Application) : AndroidViewModel(application) {
    //private val players = playerDatabase.getPlayerList()
    private val spinnerPlayerNamesData = MutableLiveData<List<String>>()

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToGameDays = MutableLiveData<Boolean>()
    val navigateToGameDays: LiveData<Boolean>
        get() = _navigateToGameDays

    fun onSaveClicked() {
        _navigateToGameDays.value = true
    }

    fun onNavigatedToGames() {
        _navigateToGameDays.value = false
    }

    fun fetchSpinnerPlayerNames(): LiveData<List<String>> {
        //fetch data
        uiScope.launch {
            spinnerPlayerNamesData.value = getPlayersName()
        }
        return spinnerPlayerNamesData
    }

    private suspend fun getPlayersName() : MutableList<String> {
        val playerNames = mutableListOf<String>()
        withContext(Dispatchers.IO) {
            val players = playerDatabase.getPlayerList()
            for (player in players){
                playerNames.add(player.playerName)
            }
        }
        Timber.i("There are ${playerNames.size} players")
        /*for (name in playerNames){
            Timber.i(name)
        }*/
        return playerNames
    }
}