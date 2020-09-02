package com.example.tennisteamtracker.database

import androidx.room.*

@Dao
interface PlayerDatabaseDao {
    @Query("select * from Player")
    fun getPlayerList(): List<Player>

    @Insert
    fun insertPlayer(player: Player)

    @Update
    fun updatePlayer(player: Player)

    @Delete
    fun deletePlayer(player: Player)

    @Query("SELECT * FROM Player ORDER BY playerId DESC LIMIT 1")
    fun getLastPlayer(): Player?

    @Transaction
    @Query("SELECT * FROM Player")
    fun getPlayersWithGames(): List<PlayersWithGames>

    @Query("SELECT COUNT(playerId) FROM Player")
    fun getNumPlayers(): Int

    @Query("DELETE FROM Player")
    fun clearAllPlayers()
}

@Dao
interface GameDayDatabaseDao {
    @Query("select * from GameDay")
    fun getGameDayList(): List<GameDay>

    @Insert
    fun insertGameDay(gameDay: GameDay)

    @Update
    fun updateGameDay(gameDay: GameDay)

    @Delete
    fun deleteGameDay(gameDay: GameDay)

    @Query("SELECT * FROM GameDay ORDER BY gameDayId DESC LIMIT 1")
    fun getLastGameDay(): GameDay?

    @Transaction
    @Query("SELECT * FROM GameDay")
    fun getGameDayWithGames(): List<GameDayWithGames>

    @Query("SELECT COUNT(gameDayId) FROM GameDay")
    fun getNumGameDays(): Int

    @Query("DELETE FROM GameDay")
    fun clearAllGameDay()
}

@Dao
interface GameDatabaseDao {
    @Query("select * from Game")
    fun getGameList(): List<Game>

    @Insert
    fun insertGame(game: Game)

    @Update
    fun updateGame(game: Game)

    @Delete
    fun deleteGame(game: Game)

    @Query("SELECT * FROM game ORDER BY gameId DESC LIMIT 1")
    fun getLastGame(): Game?
}