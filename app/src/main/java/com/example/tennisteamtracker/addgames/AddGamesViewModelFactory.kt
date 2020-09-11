package com.example.tennisteamtracker.addgames

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tennisteamtracker.addgameday.AddGameDayViewModel
import com.example.tennisteamtracker.database.GameDatabaseDao
import com.example.tennisteamtracker.database.GameDayDatabaseDao
import com.example.tennisteamtracker.database.PlayerDatabaseDao

class AddGamesViewModelFactory(
    private val dataSource: GameDayDatabaseDao,
    private val playerDataSource: PlayerDatabaseDao,
    private val gameDataSource: GameDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddGamesViewModel::class.java)) {
            return AddGamesViewModel(dataSource, playerDataSource, gameDataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}