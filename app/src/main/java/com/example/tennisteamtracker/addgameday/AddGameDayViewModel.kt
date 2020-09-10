package com.example.tennisteamtracker.addgameday

import android.app.Application
import androidx.lifecycle.*
import com.example.tennisteamtracker.database.*
import com.example.tennisteamtracker.formatPlayers
import kotlinx.coroutines.*
import timber.log.Timber

class AddGameDayViewModel(val database: GameDayDatabaseDao, val playerDatabase: PlayerDatabaseDao, val gameDatabase: GameDatabaseDao, application: Application) : AndroidViewModel(application) {
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

    fun saveNewGame(game: Game) {
        viewModelScope.launch{
            insertGame(game)
            Timber.i("New Game Added")
        }
    }

    private suspend fun insertGame(game: Game) {
        withContext(Dispatchers.IO) {
            gameDatabase.insertGame(game)
        }
    }

    fun saveNewGameDay(gameday: GameDay) {
        viewModelScope.launch{
            insertGameDay(gameday)
            Timber.i("New Game Day Added")
        }
    }

    private suspend fun insertGameDay(gameday: GameDay) {
        withContext(Dispatchers.IO) {
            database.insertGameDay(gameday)
        }
    }
}