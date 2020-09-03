package com.example.tennisteamtracker.games

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tennisteamtracker.R
import com.example.tennisteamtracker.database.EntityDatabase
import com.example.tennisteamtracker.databinding.FragmentGamesBinding
import com.example.tennisteamtracker.databinding.FragmentRosterBinding
import com.example.tennisteamtracker.roster.RosterViewModel
import com.example.tennisteamtracker.roster.RosterViewModelFactory
import timber.log.Timber

class GamesFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentGamesBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_games, container, false)
        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = EntityDatabase.getInstance(application).gameDayDatabaseDao
        val viewModelFactory = GameDaysViewModelFactory(dataSource, application)

        // Get a reference to the ViewModel associated with this fragment.
        val gameDaysViewModel = ViewModelProvider(this, viewModelFactory).get(GameDaysViewModel::class.java)

        //Connect ViewModel with UI
        binding.setLifecycleOwner(this)
        binding.gameDaysViewModel = gameDaysViewModel

        gameDaysViewModel.navigateToAdd.observe(viewLifecycleOwner,
            Observer<Boolean> { navigate ->
                if(navigate) {
                    val navController = findNavController()
                    navController.navigate(R.id.action_gamesFragment_to_addGameDayFragment)
                    gameDaysViewModel.onNavigatedToAdd()
                    Timber.i("Moved to Add")
                }
            })

        return binding.root
    }
}