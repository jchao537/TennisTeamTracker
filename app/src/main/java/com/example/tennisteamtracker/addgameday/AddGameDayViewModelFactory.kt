package com.example.tennisteamtracker.addgameday

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tennisteamtracker.addnewplayer.AddNewPlayerViewModel
import com.example.tennisteamtracker.database.GameDatabaseDao
import com.example.tennisteamtracker.database.GameDayDatabaseDao
import com.example.tennisteamtracker.database.Player
import com.example.tennisteamtracker.database.PlayerDatabaseDao

class AddGameDayViewModelFactory(
    private val dataSource: GameDayDatabaseDao,
    private val playerDataSource: PlayerDatabaseDao,
    private val gameDataSource: GameDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddGameDayViewModel::class.java)) {
            return AddGameDayViewModel(dataSource, playerDataSource, gameDataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}