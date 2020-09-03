package com.example.tennisteamtracker.games

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tennisteamtracker.database.GameDayDatabaseDao
import com.example.tennisteamtracker.database.PlayerDatabaseDao
import com.example.tennisteamtracker.roster.RosterViewModel

class GameDaysViewModelFactory(private val dataSource: GameDayDatabaseDao, private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameDaysViewModel::class.java)) {
            return GameDaysViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}