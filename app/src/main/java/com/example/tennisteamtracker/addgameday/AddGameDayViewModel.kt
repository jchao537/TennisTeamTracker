package com.example.tennisteamtracker.addgameday

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tennisteamtracker.database.Game
import com.example.tennisteamtracker.database.GameDayDatabaseDao
import com.example.tennisteamtracker.database.Player
import com.example.tennisteamtracker.database.PlayerDatabaseDao
import kotlinx.coroutines.*
import timber.log.Timber

class AddGameDayViewModel(val database: GameDayDatabaseDao, application: Application) : AndroidViewModel(application) {
    private val _navigateToGameDays = MutableLiveData<Boolean>()
    val navigateToGameDays: LiveData<Boolean>
        get() = _navigateToGameDays

    fun onSaveClicked() {
        _navigateToGameDays.value = true
    }

    fun onNavigatedToGames() {
        _navigateToGameDays.value = false
    }
}