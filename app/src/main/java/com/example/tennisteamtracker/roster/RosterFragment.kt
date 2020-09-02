package com.example.tennisteamtracker.roster

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
import com.example.tennisteamtracker.databinding.FragmentRosterBinding
import timber.log.Timber

class RosterFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentRosterBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_roster, container, false)
        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = EntityDatabase.getInstance(application).playerDatabaseDao
        val viewModelFactory = RosterViewModelFactory(dataSource, application)

        // Get a reference to the ViewModel associated with this fragment.
        val rosterViewModel = ViewModelProvider(this, viewModelFactory).get(RosterViewModel::class.java)

        //Connect ViewModel with UI
        binding.setLifecycleOwner(this)
        binding.rosterViewModel = rosterViewModel

        rosterViewModel.navigateToAdd.observe(viewLifecycleOwner,
            Observer<Boolean> { navigate ->
                if(navigate) {
                    val navController = findNavController()
                    navController.navigate(R.id.action_rosterFragment_to_addNewPlayerFragment)
                    rosterViewModel.onNavigatedToAdd()
                    Timber.i("Moved to Add")
                }
            })

        return binding.root
    }
}