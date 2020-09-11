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
import kotlinx.coroutines.delay
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

        binding.setLifecycleOwner(this)
        binding.addGameDayViewModel = addGameDayViewModel

        addGameDayViewModel.navigateToNewGames.observe(viewLifecycleOwner,
            Observer<Boolean> { navigate ->
                if(navigate) {
                    Timber.i("New Games clicked!")
                    if (TextUtils.isEmpty(opponent_name_val.text)) {
                        Toast.makeText(activity, "Please enter opponent name", Toast.LENGTH_SHORT).show()
                        Timber.i("Need Opponent Name!")
                    }
                    else {
                        //Create a new GameDay and save it into the Room
                        val newGameDay = GameDay(
                            opponentTeam = opponent_name_val.text.toString()
                        )
                        addGameDayViewModel.saveNewGameDay(newGameDay)

                        /*
                        //TESTING
                        Timber.i(binding.firstGameSpinner.selectedItem.toString())
                        var testPlayer = addGameDayViewModel.getPlayerWithLastName(binding.firstGameSpinner.selectedItem.toString())
                        Timber.i("Game 1: ${testPlayer.playerName} at rank ${testPlayer.playerRank}: ${binding.ownScore1.text}-${binding.opponentScore1.text}")
                        Timber.i("Game 2: ${binding.secondGameSpinner.selectedItem.toString()}: ${binding.ownScore2.text}-${binding.opponentScore2.text}")
                        Timber.i("Game 3: ${binding.thirdGameSpinner.selectedItem.toString()}: ${binding.ownScore3.text}-${binding.opponentScore3.text}")
                        var lastGD = addGameDayViewModel.fetchLastGameDay()
                        if (lastGD != null) {
                            Timber.i("GameDay: ${lastGD.opponentTeam}")
                        }*/

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
                        navController.navigate(R.id.action_addGameDayFragment_to_addGamesFragment)
                        addGameDayViewModel.onNavigatedToGames()
                        Timber.i("Moved to New Games")
                    }
                }
            })
        return binding.root
    }
}