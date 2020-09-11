package com.example.tennisteamtracker.addgames

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tennisteamtracker.database.*
import kotlinx.coroutines.*
import timber.log.Timber

class AddGamesViewModel(val database: GameDayDatabaseDao, val playerDatabase: PlayerDatabaseDao, val gameDatabase: GameDatabaseDao, application: Application) : AndroidViewModel(application) {
    //private val players = playerDatabase.getPlayerList()
    private val spinnerPlayerNamesData = MutableLiveData<List<String>>()
    private var player = Player()

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
        //Timber.i("There are ${playerNames.size} players")
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

    fun getPlayerWithLastName(name: String): Player {
        viewModelScope.launch{
            player = findPlayerWithLastName(name)
        }
        return player
    }

    private suspend fun findPlayerWithLastName(name: String): Player {
        var player = Player()
        withContext(Dispatchers.IO){
            player = playerDatabase.getPlayerWithLastName(name)
            //Timber.i("Found ${player.playerName} with rank ${player.playerRank}")
        }
        return player
    }

    fun updateGD(gameday: GameDay) {
        viewModelScope.launch{
            update(gameday)
        }
    }

    private suspend fun update(gameday: GameDay) {
        database.updateGameDay(gameday)
    }
}