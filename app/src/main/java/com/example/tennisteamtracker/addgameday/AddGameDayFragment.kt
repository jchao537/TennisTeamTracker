package com.example.tennisteamtracker.addgameday

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tennisteamtracker.R
import com.example.tennisteamtracker.addnewplayer.AddNewPlayerViewModel
import com.example.tennisteamtracker.addnewplayer.AddNewPlayerViewModelFactory
import com.example.tennisteamtracker.database.EntityDatabase
import com.example.tennisteamtracker.databinding.FragmentAddGameDayBinding
import com.example.tennisteamtracker.databinding.FragmentAddNewPlayerBinding
import kotlinx.android.synthetic.main.fragment_add_game_day.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class AddGameDayFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentAddGameDayBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_game_day, container, false)
        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = EntityDatabase.getInstance(application).gameDayDatabaseDao
        val playersDataSource = EntityDatabase.getInstance(application).playerDatabaseDao
        val viewModelFactory = AddGameDayViewModelFactory(dataSource, playersDataSource, application)

        // Get a reference to the ViewModel associated with this fragment.
        val addGameDayViewModel = ViewModelProvider(this, viewModelFactory).get(
            AddGameDayViewModel::class.java)

        binding.setLifecycleOwner(this)
        binding.addGameDayViewModel = addGameDayViewModel

        addGameDayViewModel.fetchSpinnerPlayerNames().observe(this.requireActivity(), Observer { spinnerData ->
            val spinnerAdapter = ArrayAdapter<String>(this.requireActivity(), android.R.layout.simple_spinner_item, spinnerData)
            binding.firstGameSpinner.adapter = spinnerAdapter
            binding.secondGameSpinner.adapter = spinnerAdapter
        })

        binding.firstGameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(activity, "Nothing Selected", Toast.LENGTH_SHORT).show()
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                Toast.makeText(activity, "You selected ${parent?.getItemAtPosition(pos).toString()}", Toast.LENGTH_SHORT).show()
            }
        }

        binding.secondGameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(activity, "Nothing Selected", Toast.LENGTH_SHORT).show()
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                Toast.makeText(activity, "You selected ${parent?.getItemAtPosition(pos).toString()}", Toast.LENGTH_SHORT).show()
            }
        }

        addGameDayViewModel.navigateToGameDays.observe(viewLifecycleOwner,
            Observer<Boolean> { navigate ->
                if(navigate) {
                    val navController = findNavController()
                    navController.navigate(R.id.action_addGameDayFragment_to_gamesFragment)
                    addGameDayViewModel.onNavigatedToGames()
                    Timber.i("Moved to Add")
                }
            })
        return binding.root
    }
}