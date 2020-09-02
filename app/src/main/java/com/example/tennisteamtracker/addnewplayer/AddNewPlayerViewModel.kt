package com.example.tennisteamtracker.addnewplayer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.tennisteamtracker.database.PlayerDatabaseDao

class AddNewPlayerViewModel(val database: PlayerDatabaseDao, application: Application) : AndroidViewModel(application) {

}