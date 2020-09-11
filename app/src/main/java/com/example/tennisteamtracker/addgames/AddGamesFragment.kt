package com.example.tennisteamtracker.addgames

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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tennisteamtracker.R
import com.example.tennisteamtracker.database.EntityDatabase
import com.example.tennisteamtracker.database.Game
import com.example.tennisteamtracker.database.GameDay
import com.example.tennisteamtracker.databinding.FragmentAddGamesBinding
import kotlinx.android.synthetic.main.fragment_add_games.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class AddGamesFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentAddGamesBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_games, container, false)
        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = EntityDatabase.getInstance(application).gameDayDatabaseDao
        val playersDataSource = EntityDatabase.getInstance(application).playerDatabaseDao
        val gameDataSource = EntityDatabase.getInstance(application).gameDatabaseDao
        val viewModelFactory = AddGamesViewModelFactory(dataSource, playersDataSource, gameDataSource, application)

        // Get a reference to the ViewModel associated with this fragment.
        val addGamesViewModel = ViewModelProvider(this, viewModelFactory).get(
            AddGamesViewModel::class.java)

        var lastGameDay = GameDay(gameDayId = -1)

        GlobalScope.launch {
            lastGameDay = dataSource.getLastGameDay()!!
        }

        var listOfSpinners = listOf<Spinner>(binding.firstGameSpinner, binding.secondGameSpinner, binding.thirdGameSpinner)
        var listOfEditTexts = listOf<EditText>(binding.opponentScore1, binding.opponentScore2, binding.ownScore1,
            binding.ownScore2, binding.opponentScore3, binding.ownScore3)

        binding.setLifecycleOwner(this)
        binding.addGamesViewModel = addGamesViewModel

        addGamesViewModel.fetchSpinnerPlayerNames().observe(this.requireActivity(), Observer { spinnerData ->
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
                    //Toast.makeText(activity, "You selected ${parent?.getItemAtPosition(pos).toString()}", Toast.LENGTH_SHORT).show()
                    //var selectedPlayer = addGameDayViewModel.ge().getValue().get(pos)
                }
            }
        }

        addGamesViewModel.navigateToGameDays.observe(viewLifecycleOwner,
            Observer<Boolean> { navigate ->
                if(navigate) {
                    Timber.i("Save clicked!")
                    var allValuesValid = true
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
                        //Create and Add New Game 1
                        var playId = addGamesViewModel.getPlayerWithLastName(binding.firstGameSpinner.selectedItem.toString())
                        val newGame = Game(
                            gamePlayerId = playId.playerId,
                            partOfGameDayId = lastGameDay.gameDayId,
                            ownScore = Integer.parseInt(own_score_1.text.toString()),
                            opponentScore = Integer.parseInt(opponent_score_1.text.toString()),
                            isAWin = Integer.parseInt(own_score_1.text.toString()) > Integer.parseInt(opponent_score_1.text.toString())
                        )
                        addGamesViewModel.saveNewGame(newGame)

                        val gd = GameDay(
                            gameDayId = lastGameDay.gameDayId,
                            opponentTeam = lastGameDay.opponentTeam,
                            teamWins = numWins,
                            teamLosses = 3 - numWins,
                            isATeamWin = gameDayWin
                        )
                        addGamesViewModel.updateGD(gd)

                        //Navigate back to the Game Fragment
                        val navController = findNavController()
                        navController.navigate(R.id.action_addGamesFragment_to_gamesFragment)
                        addGamesViewModel.onNavigatedToGames()
                        Timber.i("Moved to Games")
                    }
                }
            })
        return binding.root
    }
}