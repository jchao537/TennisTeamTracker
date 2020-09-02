package com.example.tennisteamtracker.addnewplayer

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tennisteamtracker.R
import com.example.tennisteamtracker.database.EntityDatabase
import com.example.tennisteamtracker.database.Player
import com.example.tennisteamtracker.databinding.FragmentAddNewPlayerBinding
import com.example.tennisteamtracker.hideSoftKeyboard
import kotlinx.android.synthetic.main.fragment_add_new_player.*
import timber.log.Timber

class AddNewPlayerFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentAddNewPlayerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_new_player, container, false)
        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = EntityDatabase.getInstance(application).playerDatabaseDao
        val viewModelFactory = AddNewPlayerViewModelFactory(dataSource, application)

        // Get a reference to the ViewModel associated with this fragment.
        val addNewPlayerViewModel = ViewModelProvider(this, viewModelFactory).get(
            AddNewPlayerViewModel::class.java)

        binding.setLifecycleOwner(this)
        binding.addNewPlayerViewModel = addNewPlayerViewModel

        addNewPlayerViewModel.navigateToRoster.observe(viewLifecycleOwner,
            Observer<Boolean> { navigate ->
                if(navigate) {
                    Timber.i("Save clicked!")
                    if (TextUtils.isEmpty(edit_name.text)) {
                        Toast.makeText(activity,"Please enter name",Toast.LENGTH_SHORT).show()
                        Timber.i("Need Name!")
                    } else if (TextUtils.isEmpty(edit_year.text)){
                        Toast.makeText(activity,"Please enter year",Toast.LENGTH_SHORT).show()
                        Timber.i("Need Year!")
                    } else if (TextUtils.isEmpty(edit_rank.text)){
                        Toast.makeText(activity,"Please enter rank",Toast.LENGTH_SHORT).show()
                        Timber.i("Need Rank!")
                    } else {
                        val newPlayer = Player(
                            playerName = edit_name.text.toString(),
                            playerAge = edit_year.text.toString().toInt(),
                            playerRank = edit_rank.text.toString().toInt()
                        )
                        addNewPlayerViewModel.saveNewPlayer(newPlayer)
                        Timber.i("New Player Added 2")
                        findNavController().navigate(R.id.action_addNewPlayerFragment_to_rosterFragment)
                        addNewPlayerViewModel.onNavigatedToRoster()
                        hideSoftKeyboard(requireActivity())
                    }
                }
            })
        return binding.root
    }
}