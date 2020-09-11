package com.example.tennisteamtracker.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlayerDatabaseDao {
    @Query("select * from Player")
    fun getPlayerList(): List<Player>

    @Insert
    suspend fun insertPlayer(player: Player)

    @Update
    suspend fun updatePlayer(player: Player)

    @Delete
    suspend fun deletePlayer(player: Player)

    @Query("SELECT * FROM Player ORDER BY playerId DESC LIMIT 1")
    suspend fun getLastPlayer(): Player?

    @Transaction
    @Query("SELECT * FROM Player")
    fun getPlayersWithGames(): List<PlayersWithGames>

    @Query("SELECT * FROM Player")
    fun getAllPlayersAsLiveData(): LiveData<List<Player>>

    @Transaction
    @Query("SELECT * FROM Player")
    fun getPlayersWithGamesAsLiveData(): LiveData<List<PlayersWithGames>>

    @Query("SELECT COUNT(playerId) FROM Player")
    suspend fun getNumPlayers(): Int

    @Query("DELETE FROM Player")
    suspend fun clearAllPlayers()

    @Query("SELECT * FROM Player WHERE playerName = :name")
    suspend fun getPlayerWithLastName(name: String): Player
}

@Dao
interface GameDayDatabaseDao {
    @Query("select * from GameDay")
    fun getGameDayList(): List<GameDay>

    @Insert
    suspend fun insertGameDay(gameDay: GameDay)

    @Update
    suspend fun updateGameDay(gameDay: GameDay)

    @Delete
    suspend fun deleteGameDay(gameDay: GameDay)

    @Query("SELECT * FROM GameDay ORDER BY gameDayId DESC LIMIT 1")
    suspend fun getLastGameDay(): GameDay?

    @Transaction
    @Query("SELECT * FROM GameDay")
    fun getGameDayWithGames(): List<GameDayWithGames>

    @Query("SELECT * FROM GameDay")
    fun getAllGameDaysAsLiveData(): LiveData<List<GameDay>>

    @Transaction
    @Query("SELECT * FROM GameDay")
    fun getGameDayWithGamesAsLiveData(): LiveData<List<GameDayWithGames>>

    @Query("SELECT COUNT(gameDayId) FROM GameDay")
    suspend fun getNumGameDays(): Int

    @Query("DELETE FROM GameDay")
    suspend fun clearAllGameDay()
}

@Dao
interface GameDatabaseDao {
    @Query("select * from Game")
    fun getGameList(): List<Game>

    @Query("select * from Game")
    fun getGameListAsLiveData(): LiveData<List<Game>>

    @Insert
    suspend fun insertGame(game: Game)

    @Update
    suspend fun updateGame(game: Game)

    @Delete
    suspend fun deleteGame(game: Game)

    @Query("SELECT * FROM game ORDER BY gameId DESC LIMIT 1")
    suspend fun getLastGame(): Game?
}