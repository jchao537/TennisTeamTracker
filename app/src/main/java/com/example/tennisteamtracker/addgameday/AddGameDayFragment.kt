package com.example.tennisteamtracker.addgameday

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.room.Query
import com.example.tennisteamtracker.R
import com.example.tennisteamtracker.addnewplayer.AddNewPlayerViewModel
import com.example.tennisteamtracker.addnewplayer.AddNewPlayerViewModelFactory
import com.example.tennisteamtracker.database.EntityDatabase
import com.example.tennisteamtracker.database.Game
import com.example.tennisteamtracker.database.GameDay
import com.example.tennisteamtracker.database.Player
import com.example.tennisteamtracker.databinding.FragmentAddGameDayBinding
import com.example.tennisteamtracker.databinding.FragmentAddNewPlayerBinding
import com.example.tennisteamtracker.hideSoftKeyboard
import kotlinx.android.synthetic.main.fragment_add_game_day.*
import kotlinx.android.synthetic.main.fragment_add_new_player.*
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
        val gameDataSource = EntityDatabase.getInstance(application).gameDatabaseDao
        val viewModelFactory = AddGameDayViewModelFactory(dataSource, playersDataSource, gameDataSource, application)

        // Get a reference to the ViewModel associated with this fragment.
        val addGameDayViewModel = ViewModelProvider(this, viewModelFactory).get(
            AddGameDayViewModel::class.java)

        var listOfSpinners = listOf<Spinner>(binding.firstGameSpinner, binding.secondGameSpinner, binding.thirdGameSpinner)
        var listOfEditTexts = listOf<EditText>(binding.opponentScore1, binding.opponentScore2, binding.ownScore1,
                                                binding.ownScore2, binding.opponentScore3, binding.ownScore3)

        binding.setLifecycleOwner(this)
        binding.addGameDayViewModel = addGameDayViewModel

        addGameDayViewModel.fetchSpinnerPlayerNames().observe(this.requireActivity(), Observer { spinnerData ->
            val spinnerAdapter = ArrayAdapter<String>(this.requireActivity(), android.R.layout.simple_spinner_item, spinnerData)
            for (spinner in listOfSpinners) {
                spinner.adapter = spinnerAdapter
            }
        })

        for (spinner in listOfSpinners){
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Toast.makeText(activity, "Nothing Selected", Toast.LENGTH_SHORT).show()
                }

                override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                    Toast.makeText(activity, "You selected ${parent?.getItemAtPosition(pos).toString()}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        addGameDayViewModel.navigateToGameDays.observe(viewLifecycleOwner,
            Observer<Boolean> { navigate ->
                if(navigate) {
                    Timber.i("Save clicked!")
                    var allValuesValid = true
                    if (TextUtils.isEmpty(opponent_name_val.text)) {
                        Toast.makeText(activity, "Please enter opponent name", Toast.LENGTH_SHORT).show()
                        Timber.i("Need Opponent Name!")
                        allValuesValid = false
                    }
                    for (editText in listOfEditTexts) {
                        if (TextUtils.isEmpty(editText.text)) {
                            Toast.makeText(activity, "Please enter all scores", Toast.LENGTH_SHORT).show()
                            Timber.i("Need All Scores!")
                            allValuesValid = false
                        }
                    }
                    if (allValuesValid) {
                        var numWins = 0
                        var gameDayWin = false
                        if (Integer.parseInt(own_score_1.text.toString()) > Integer.parseInt(opponent_score_1.text.toString())){
                            numWins++
                        }
                        if (Integer.parseInt(own_score_2.text.toString()) > Integer.parseInt(opponent_score_2.text.toString())){
                            numWins++
                        }
                        if (Integer.parseInt(own_score_3.text.toString()) > Integer.parseInt(opponent_score_3.text.toString())){
                            numWins++
                        }
                        if (numWins > (3 - numWins)){
                            gameDayWin = true
                        }

                        //Create a new GameDay and save it into the Room
                        val newGameDay = GameDay(
                            opponentTeam = opponent_name_val.text.toString(),
                            teamWins = numWins,
                            teamLosses = 3 - numWins,
                            isATeamWin = gameDayWin
                        )
                        addGameDayViewModel.saveNewGameDay(newGameDay)
                        /*
                        val gameDayId = addGameDayViewModel.getLastGameDayId()

                        //Create and Add a New Game
                        var playId = addGameDayViewModel.getSpecificPlayerId(binding.firstGameSpinner.selectedItem.toString())
                        val newGame = Game(
                            gamePlayerId = playId,
                            partOfGameDayId = gameDayId,
                            ownScore = Integer.parseInt(own_score_1.text.toString()),
                            opponentScore = Integer.parseInt(opponent_score_1.text.toString()),
                            isAWin = Integer.parseInt(own_score_1.text.toString()) > Integer.parseInt(opponent_score_1.text.toString())
                        )
                        addGameDayViewModel.saveNewGame(newGame)*/

                        //Navigate back to the Game Fragment
                        val navController = findNavController()
                        navController.navigate(R.id.action_addGameDayFragment_to_gamesFragment)
                        addGameDayViewModel.onNavigatedToGames()
                        Timber.i("Moved to Games")
                    }
                }
            })
        return binding.root
    }
}