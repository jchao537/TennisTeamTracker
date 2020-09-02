package com.example.tennisteamtracker.addnewplayer

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tennisteamtracker.database.PlayerDatabaseDao

class AddNewPlayerViewModelFactory(
    private val dataSource: PlayerDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddNewPlayerViewModel::class.java)) {
            return AddNewPlayerViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}