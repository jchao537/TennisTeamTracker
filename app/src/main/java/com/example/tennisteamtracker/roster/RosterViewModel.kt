package com.example.tennisteamtracker.roster

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tennisteamtracker.database.PlayerDatabaseDao

class RosterViewModel(val database: PlayerDatabaseDao, application: Application) : AndroidViewModel(application) {
    private val _navigateToAdd = MutableLiveData<Boolean>()
    val navigateToAdd: LiveData<Boolean>
        get() = _navigateToAdd

    fun onFabClicked() {
        _navigateToAdd.value = true
    }

    fun onNavigatedToAdd() {
        _navigateToAdd.value = false
    }
}
