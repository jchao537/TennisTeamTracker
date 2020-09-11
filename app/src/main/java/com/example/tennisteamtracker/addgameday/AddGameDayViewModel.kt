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
    private var player = Player()
    private var gameday = MutableLiveData<GameDay?>()

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToNewGames = MutableLiveData<Boolean>()
    val navigateToNewGames: LiveData<Boolean>
        get() = _navigateToNewGames

    fun onSaveClicked() {
        _navigateToNewGames.value = true
    }

    fun onNavigatedToGames() {
        _navigateToNewGames.value = false
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
            Timber.i("inserted gameday against ${gameday.opponentTeam}")
        }
    }

    fun fetchLastGameDay(): GameDay? {
        viewModelScope.launch{
            gameday.value = getLastGD()
        }
        return gameday.value
    }

    private suspend fun getLastGD(): GameDay? {
        withContext(Dispatchers.IO) {
            Timber.i("There are ${database.getGameDayList().size} gamedays")
        }
        var gd = database.getLastGameDay()
        if (gd != null) {
            Timber.i("Last gameday was against ${gd.opponentTeam} which had ${gd.teamWins} team wins")
        }
        return gd
    }
}